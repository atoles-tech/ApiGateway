package atl.web.api_gateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthRequestDto {

    @NotBlank(message = "Email must not be blank")
    @Size(min = 5,message = "Email must be more than 5 characters")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8,message = "Password must be more than 8 characters")
    private String password;
}
