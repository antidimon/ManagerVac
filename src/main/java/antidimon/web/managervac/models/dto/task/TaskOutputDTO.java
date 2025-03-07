package antidimon.web.managervac.models.dto.task;

import antidimon.web.managervac.models.dto.comment.CommentOutputDTO;
import antidimon.web.managervac.models.dto.user.MyUserIdNameOutputDTO;
import antidimon.web.managervac.models.enums.TaskPriority;
import antidimon.web.managervac.models.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TaskOutputDTO {

    private long id;
    private String taskName;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime deadline;
    private List<MyUserIdNameOutputDTO> developers;
    private List<CommentOutputDTO> comments;

}
