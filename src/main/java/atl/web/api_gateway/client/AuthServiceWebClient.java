package atl.web.api_gateway.client;

import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import atl.web.api_gateway.dto.ValidateTokenRequestDto;
import reactor.core.publisher.Mono;

@Component
public class AuthServiceWebClient {
    
    private final WebClient webClient;
    
    public AuthServiceWebClient(WebClient.Builder webClientBuilder,
                              LoadBalancedExchangeFilterFunction loadBalancerFilter) {
        this.webClient = webClientBuilder
            .filter(loadBalancerFilter)
            .baseUrl("lb://auth-service")
            .build();
    }
    
    public Mono<Boolean> validateToken(String token) {
        ValidateTokenRequestDto request = new ValidateTokenRequestDto(token);
        
        return webClient.post()
            .uri("/api/v1/auth/validate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Boolean.class)
            .onErrorReturn(false);
    }
}