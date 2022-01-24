package com.aks.exchange.controller;

import com.aks.exchange.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.aks.exchange.model.Order;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ExchangeController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @PostMapping("/rates/rate")
    public Order getRate(HttpServletRequest request, @RequestBody Order order){
        System.out.println("Token: " + request.getHeader("Authorization"));
        return exchangeRateService.getRate(order);
    }
}
