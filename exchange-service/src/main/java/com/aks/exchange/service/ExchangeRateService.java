package com.aks.exchange.service;

import com.aks.exchange.model.Order;

public interface ExchangeRateService {

    Order getRate(Order order);
}
