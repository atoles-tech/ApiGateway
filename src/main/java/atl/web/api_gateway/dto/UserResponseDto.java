package atl.web.api_gateway.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto implements Serializable{
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
}
