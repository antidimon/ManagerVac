package antidimon.web.managervac.models.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;


@Schema(description = "DTO для получения всей нужной информации о пользователе")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MyUserOutputDTO {

    @Schema(description = "Идентификатор пользователя", example = "1")
    private long id;
    @Schema(description = "Username пользователя", example = "Test")
    private String username;
    @Schema(description = "Почта пользователя", example = "example@mail.ru")
    private String email;
    @Schema(description = "Дата создания пользователя", example = "2025-01-01 00:00:00.000")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createdAt;
}
