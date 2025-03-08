package antidimon.web.managervac.models.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Schema(description = "DTO для регистрации пользователя")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MyUserInputDTO {

    @Schema(description = "Username пользователя", example = "Test")
    @NotBlank
    private String username;

    @Schema(description = "Почта пользователя", example = "example@mail.ru")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Пароль пользователя", example = "qfige131rAWFE@")
    @NotBlank
    @Size(min = 5, max = 30)
    private String password;

}
