package pl.piomin.samples.security.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Configuration
public class GlobalFilterConfiguration {

    @Autowired
    private RestTemplate restTemplate;

    final Logger logger = LoggerFactory.getLogger(GlobalFilterConfiguration.class);

    @Bean
    public GlobalFilter postGlobalFilter2() {
        return (exchange, chain) -> {
            if(isResponseUnauthorized(exchange)) { // TOKEN EXPIRED --> REFRESH
                String token = refreshToken(exchange.getRequest().getCookies().get("SESSION").get(0).getValue());
                logger.debug("New token created: " + token);

                ServerHttpRequest request = exchange.getRequest()
                        .mutate()
                        .header("Authorization", String.format("Bearer %s", token))
                        .build();

                ServerWebExchange exchange1 = exchange.mutate().request(request).build();
                return chain.filter(exchange1);
            }
            return chain.filter(exchange);
        };
    }

    private boolean isResponseUnauthorized(final ServerWebExchange exchange) {
        return exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    public String refreshToken(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "SESSION=" + sessionId);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        String url = "http://localhost:8060/token";
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<AccessTokenResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, AccessTokenResponse.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            logger.info("Token refreshed");
        } else {
            logger.error("Token refresh FAILED");
        }
        return response.getBody().getAccessToken();
    }
}
