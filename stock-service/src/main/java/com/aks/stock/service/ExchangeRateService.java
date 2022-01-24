package com.aks.stock.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.aks.stock.model.Order;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    @Autowired
    @Qualifier("exchange-client")
    private WebClient webClient;

    public Order calculateQuantity(final Order order, String token, String sessionId) {
        Mono<Order> response = webClient
                .post()
                .uri("/rates/rate")
                .bodyValue(order)
                .header("Authorization", token)// Set if directly to the Service
                .cookie("SESSION", sessionId)// Set if via API Gateway
                .retrieve().bodyToMono(Order.class);
        Order rate = response.block();

        order.setBuyPrice(rate.getBuyPrice());
        order.setQuantity(order.getAmount().divide(rate.getBuyPrice(), 2, RoundingMode.UP));
        return order;
    }

}