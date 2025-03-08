package antidimon.web.managervac.models.dto.project;

import antidimon.web.managervac.models.dto.task.TaskOutputDTO;
import antidimon.web.managervac.models.dto.user.MyUserIdNameOutputDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Schema(description = "DTO для получения проекта из бд")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProjectOutputDTO {

    @Schema(description = "Идентификатор проекта", example = "1")
    private long id;
    @Schema(description = "Имя проекта", example = "Test")
    private String name;
    @Schema(description = "Дата создания проекта", example = "2025-01-01 00:00:00.000")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createdAt;
    @Schema(description = "Участники проекта")
    private List<MyUserIdNameOutputDTO> members;
    @Schema(description = "Задачи проекта")
    private List<TaskOutputDTO> tasks;
}
