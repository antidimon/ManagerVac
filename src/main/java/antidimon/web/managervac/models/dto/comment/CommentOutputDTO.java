package antidimon.web.managervac.models.dto.comment;


import antidimon.web.managervac.models.dto.user.MyUserIdNameOutputDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;


@Schema(description = "DTO для получения сообщения из бд")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CommentOutputDTO {

    @Schema(description = "Идентификатор сообщения", example = "1")
    private long id;
    @Schema(description = "Отправитель сообщения")
    private MyUserIdNameOutputDTO user;
    @Schema(description = "Текст сообщения", example = "Hello")
    private String comment;
    @Schema(description = "Дата отправки сообщения", example = "2025-01-01 00:00:00.000")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createdAt;
}
