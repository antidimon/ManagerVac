package antidimon.web.managervac.models.dto.task;


import antidimon.web.managervac.models.enums.TaskPriority;
import antidimon.web.managervac.models.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TaskInputDTO {

    @NotNull
    private long projectId;
    @NotBlank
    private String taskName;
    @NotBlank
    private String description;
    @NotNull
    private TaskStatus status;
    @NotNull
    private TaskPriority priority;
    @NotNull
    private LocalDateTime deadline;

}
