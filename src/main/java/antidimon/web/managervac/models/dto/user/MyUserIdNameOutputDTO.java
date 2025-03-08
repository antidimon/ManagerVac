package antidimon.web.managervac.models.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "DTO для получение минимальной информации о пользователе из бд")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MyUserIdNameOutputDTO {

    @Schema(description = "Идентификатор пользователя", example = "1")
    private long id;
    @Schema(description = "Username пользователя", example = "Test")
    private String username;

}
