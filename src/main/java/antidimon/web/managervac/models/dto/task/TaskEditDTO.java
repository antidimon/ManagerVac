package antidimon.web.managervac.models.dto.task;


import antidimon.web.managervac.models.enums.TaskPriority;
import antidimon.web.managervac.models.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "DTO для изменения задачи")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskEditDTO {

    @Schema(description = "Новое имя задачи", example = "Test")
    private String taskName;
    @Schema(description = "Новое описание задачи", example = "Test task")
    private String description;
    @Schema(description = "Новый статус задачи", example = "TODO")
    private TaskStatus status;
    @Schema(description = "Новый приоритет задачи", example = "HIGH")
    private TaskPriority priority;
    @Schema(description = "Новая дата дедлайна", example = "2025-01-01 00:00:00.000")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime deadline;

}
