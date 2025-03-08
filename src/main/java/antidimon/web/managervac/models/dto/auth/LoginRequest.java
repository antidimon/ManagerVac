package antidimon.web.managervac.models.dto.auth;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Форма аутентификации")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {

    @Schema(description = "Почта пользователя", example = "example@mail.ru")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Пароль пользователя", example = "qfige131rAWFE@")
    @NotBlank
    @Size(min = 5, max = 30)
    private String password;
}
