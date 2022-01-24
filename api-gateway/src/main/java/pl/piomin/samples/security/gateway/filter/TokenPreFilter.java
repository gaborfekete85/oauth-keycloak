package pl.piomin.samples.security.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-1000)
public class TokenPreFilter implements GlobalFilter {
    final Logger logger = LoggerFactory.getLogger(TokenPreFilter.class);

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .header("GATEWAY_SESSION", exchange.getRequest().getCookies().get("SESSION").get(0).getValue())
                .build();
        ServerWebExchange exchange1 = exchange.mutate().request(request).build();

        return chain.filter(exchange1);
    }
}
