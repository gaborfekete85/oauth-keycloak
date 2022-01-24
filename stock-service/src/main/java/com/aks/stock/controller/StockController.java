package com.aks.stock.controller;

import com.aks.stock.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.aks.stock.model.Order;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Set;

@RestController
public class StockController {
    private static final Set<String> SUPPORTED_SYMBOLS = Set.of("AMZN", "TSLA", "AAPL");

    @Autowired
    private ExchangeRateService exchangeRateService;

    @PostMapping("/order")
    @PreAuthorize("hasAuthority('SCOPE_email')")
    public Order createOrder(HttpServletRequest request, @RequestBody Order order){
        System.out.println("Token: " + request.getHeader("Authorization"));
        order.setId(1);
        if(!SUPPORTED_SYMBOLS.contains(order.getSymbol())) {
            throw new SymbolNotSupportedException(String.format("%s symbol not supported. Supported symbols: %s", order.getSymbol(), SUPPORTED_SYMBOLS));
        }

        Cookie sessionCookie = Arrays.stream(request.getCookies()).filter( x -> "SESSION".equals(x.getName())).findFirst().get();
        return exchangeRateService.calculateQuantity(order, request.getHeader("Authorization"), sessionCookie.getValue());
    }
}