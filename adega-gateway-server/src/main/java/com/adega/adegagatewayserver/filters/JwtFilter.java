package com.adega.adegagatewayserver.filters;

import com.adega.adegagatewayserver.models.AuthModel;
import com.adega.adegagatewayserver.validators.RouteValidator;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Configuration> {

    private final RestTemplate restTemplate;
    private final RouteValidator routeValidator;

    public JwtFilter(RestTemplate restTemplate, RouteValidator routeValidator){
        super(Configuration.class);
        this.restTemplate = restTemplate;
        this.routeValidator = routeValidator;
    }

    @Override
    public GatewayFilter apply(Configuration config){
        return (exchange,chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest())){
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Authorization header is missing!");
                }
                var authHeader = Objects.requireNonNull(
                        exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                if(authHeader.isBlank() || authHeader.isEmpty()) {
                    throw new RuntimeException("Token is missing!");
                }
                var bearerToken = authHeader.replace("Bearer ", "");
                var authModel = restTemplate.getForObject(
                        "http://localhost:5050/users/validate-token?token="
                                + bearerToken,
                        AuthModel.class);
                if (authModel == null)
                    throw new RuntimeException("Token is not valid!");
                exchange.getRequest().mutate()
                        .header("Authentication-Principal",
                                authModel.getPrincipal())
                        .header("Authentication-Credentials",
                                authModel.getCredentials())
                        .header("Authentication-Authorities",
                                authModel.getAuthorities());
            }
            return chain.filter(exchange);
        };
    }

    public static class Configuration {
    }

}
