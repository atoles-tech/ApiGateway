package atl.web.api_gateway.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrationForm {
    
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @NotNull(message = "Date of birth is required")
    private LocalDate birthDate;

    @NotBlank(message = "Email must not be blank")
    @Size(min = 5,message = "Email must be more than 5 characters")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8,message = "Password must be more than 8 characters")
    private String password;

    @NotNull(message = "Role must not be blank")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Role role;

}
