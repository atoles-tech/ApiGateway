package atl.web.api_gateway.client;

import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import atl.web.api_gateway.dto.AuthReponseDto;
import atl.web.api_gateway.dto.AuthRequestDto;
import atl.web.api_gateway.dto.RegistrationRequestDto;
import atl.web.api_gateway.dto.RegistrationResponseDto;
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

    public Mono<RegistrationResponseDto> register(RegistrationRequestDto request){
        return webClient.post()
            .uri("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(RegistrationResponseDto.class);
    }

    public Mono<AuthReponseDto> login(AuthRequestDto request){
        return webClient.post()
            .uri("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(AuthReponseDto.class);
    }

    public Mono<Void> deleteCredential(Long id){
        return webClient.delete()
            .uri("/api/v1/auth/delete/{id}", id)
            .retrieve()
            .bodyToMono(Void.class);
    }
}