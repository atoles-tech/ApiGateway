package atl.web.api_gateway.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import atl.web.api_gateway.dto.RegistrationForm;
import atl.web.api_gateway.services.UserRegistrationSaga;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
public class RegistrationController {
    
    private UserRegistrationSaga userRegistrationSaga;

    @PostMapping("/register")
    public Mono<?> register(@Valid @RequestBody RegistrationForm request){
        return userRegistrationSaga.register(request);
    }

}
