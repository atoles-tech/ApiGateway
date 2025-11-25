package atl.web.api_gateway.config;

import atl.web.api_gateway.client.AuthServiceWebClient;
import atl.web.api_gateway.dto.ErrorDto;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@AllArgsConstructor
public class JwtFilter implements GlobalFilter{

    private ObjectMapper objectMapper;
    private AuthServiceWebClient authServiceWebClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        if (List.of("/api/v1/auth/login").stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String token = extractToken(authHeader);
        
        if (token == null) {
            return tokenException(exchange, "Token not found");
        }

        return authServiceWebClient.validateToken(token)
                .flatMap(isValid -> {
                    if (isValid) {
                        return chain.filter(exchange);
                    } else {
                        return tokenException(exchange, "Invalid or expired token");
                    }
                })
                .onErrorResume(e -> {
                    return tokenException(exchange, e.getMessage());
                });
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    

    private Mono<Void> tokenException(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        
        String errorResponse;
        try {
            errorResponse = objectMapper.writeValueAsString(new ErrorDto(message));
        } catch (Exception ignored) {
            errorResponse = "{\"error\": \"Object mapper error\"}";
        }

        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorResponse.getBytes())));
    }

}