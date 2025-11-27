package atl.web.api_gateway.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class RegistrationResponse {
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
    private Role role;
    private String error;
}
