package antidimon.web.managervac.models.dto.task;


import antidimon.web.managervac.models.enums.TaskPriority;
import antidimon.web.managervac.models.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "DTO для создания задачи")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TaskInputDTO {

    @Schema(description = "Имя задачи", example = "Test")
    @NotBlank
    private String taskName;
    @Schema(description = "Описание задачи", example = "Test task")
    @NotBlank
    private String description;
    @Schema(description = "Статус задачи", example = "TODO")
    @NotNull
    private TaskStatus status;
    @Schema(description = "Приоритет задачи", example = "HIGH")
    @NotNull
    private TaskPriority priority;
    @Schema(description = "Дата дедлайна", example = "2025-01-01 00:00:00.000")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime deadline;

}
