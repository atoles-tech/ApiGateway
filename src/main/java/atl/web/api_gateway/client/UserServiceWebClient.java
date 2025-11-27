package atl.web.api_gateway.client;

import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import atl.web.api_gateway.dto.UserDto;
import atl.web.api_gateway.dto.UserResponseDto;
import reactor.core.publisher.Mono;

@Component
public class UserServiceWebClient {
    
    private final WebClient webClient;

    public UserServiceWebClient(WebClient.Builder webClientBuilder,
                              LoadBalancedExchangeFilterFunction loadBalancerFilter) {
        this.webClient = webClientBuilder
            .filter(loadBalancerFilter)
            .baseUrl("lb://user-service")
            .build();
    }

    public Mono<UserResponseDto> createUser(UserDto request, String token){
        return webClient.post()
            .uri("/api/v1/users")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(UserResponseDto.class);
    }

    public Mono<Void> deleteUser(Long id, String token){
        return webClient.delete()
            .uri("/api/v1/users/{id}", id)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToMono(Void.class);
    }

}
