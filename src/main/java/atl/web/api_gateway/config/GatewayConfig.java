package atl.web.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .and()
                        .predicate(x -> !x.getRequest().getPath().value().equals("api/v1/auth/register"))
                        // register request doesn't go to auth service
                        .uri("lb://auth-service"))
                .route("user-service", r -> r.path("/api/v1/users/**", "/api/v1/cards/**")
                        .uri("lb://user-service"))
                .route("order-service", r -> r.path("/api/v1/orders/**", "/api/v1/items/**")
                        .uri("lb://order-service"))
                .build();
    }
}