package com.aks.stock.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerProperties;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Autowired
    private ReactorLoadBalancerExchangeFilterFunction lbFunction;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public LoadBalancerClientFactory loadBalancerClientFactory() {
        return new LoadBalancerClientFactory();
    }

    @Bean
    public ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction(LoadBalancerProperties properties) {
        return new ReactorLoadBalancerExchangeFilterFunction(loadBalancerClientFactory());
    }

    @Bean
    @Qualifier("exchange-client")
    public WebClient webClient(){

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer ->
                        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper)))
                .build();

        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .filter(lbFunction)
//                .baseUrl("http://exchange-service")
                .baseUrl("http://gateway-service")
                .build();
    }
}
