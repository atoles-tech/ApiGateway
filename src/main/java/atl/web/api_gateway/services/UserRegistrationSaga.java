package atl.web.api_gateway.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import atl.web.api_gateway.client.AuthServiceWebClient;
import atl.web.api_gateway.client.UserServiceWebClient;
import atl.web.api_gateway.dto.AuthRequestDto;
import atl.web.api_gateway.dto.RegistrationForm;
import atl.web.api_gateway.dto.RegistrationRequestDto;
import atl.web.api_gateway.dto.RegistrationResponse;
import atl.web.api_gateway.dto.UserDto;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserRegistrationSaga {

    private UserServiceWebClient userServiceWebClient;
    private AuthServiceWebClient authServiceWebClient;

    public Mono<?> register(RegistrationForm request){
        RegistrationRequestDto regRequest = 
            new RegistrationRequestDto(request.getEmail(), request.getPassword(), request.getRole());
        AuthRequestDto authRequest = new AuthRequestDto(request.getEmail(), request.getPassword());
        UserDto userRequest = new UserDto(request.getName(), request.getSurname(), request.getBirthDate()); 

        return authServiceWebClient.register(regRequest)
            .flatMap(regResponse -> 
                authServiceWebClient.login(authRequest)
                    .flatMap(authResponse -> 
                        userServiceWebClient.createUser(userRequest, authResponse.getAccessToken())
                            .map(userResponse -> 
                                buildSuccessResponse(request)
                            )
                            .onErrorResume(e -> {
                                return authServiceWebClient.deleteCredential(regResponse.getId())
                                    .then(Mono.just(buildErrorResponse(((WebClientResponseException)e).getResponseBodyAsString())));
                            })
                    )
                    .onErrorResume(e -> {
                        return authServiceWebClient.deleteCredential(regResponse.getId())
                            .then(Mono.just(buildErrorResponse(((WebClientResponseException)e).getResponseBodyAsString())));
                    })
            )
            .onErrorResume(e -> {
                return Mono.just(buildErrorResponse(((WebClientResponseException)e).getResponseBodyAsString()));
            })
            .defaultIfEmpty(buildErrorResponse("Service unavailable"));
    }

    private RegistrationResponse buildSuccessResponse(RegistrationForm request) {
        return RegistrationResponse.builder()
            .email(request.getEmail())
            .name(request.getName())
            .surname(request.getSurname())
            .birthDate(request.getBirthDate())
            .role(request.getRole())
            .build();
    }

    private RegistrationResponse buildErrorResponse(String error) {
        return RegistrationResponse.builder()
            .error(error)
            .build();
    }
}