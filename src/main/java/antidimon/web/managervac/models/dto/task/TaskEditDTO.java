package antidimon.web.managervac.models.dto.task;


import antidimon.web.managervac.models.enums.TaskPriority;
import antidimon.web.managervac.models.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskEditDTO {

    private String taskName;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime deadline;

}
