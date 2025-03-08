package antidimon.web.managervac.models.dto.task;

import antidimon.web.managervac.models.dto.comment.CommentOutputDTO;
import antidimon.web.managervac.models.dto.user.MyUserIdNameOutputDTO;
import antidimon.web.managervac.models.enums.TaskPriority;
import antidimon.web.managervac.models.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Schema(description = "DTO для получения задачи из бд")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TaskOutputDTO {

    @Schema(description = "Идентификатор задачи", example = "1")
    private long id;
    @Schema(description = "Имя задачи", example = "Test")
    private String taskName;
    @Schema(description = "Описание задачи", example = "Test task")
    private String description;
    @Schema(description = "Статус задачи", example = "TODO")
    private TaskStatus status;
    @Schema(description = "Приоритет задачи", example = "HIGH")
    private TaskPriority priority;
    @Schema(description = "Дата создания", example = "2025-01-01 00:00:00.000")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createdAt;
    @Schema(description = "Дата дедлайна", example = "2025-01-01 00:00:00.000")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime deadline;
    @Schema(description = "Исполнители задачи")
    private List<MyUserIdNameOutputDTO> developers;
    @Schema(description = "Комментарии к задаче")
    private List<CommentOutputDTO> comments;

}
