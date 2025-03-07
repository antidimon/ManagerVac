package antidimon.web.managervac.models.dto.task;

import antidimon.web.managervac.models.dto.comment.CommentOutputDTO;
import antidimon.web.managervac.models.dto.user.MyUserIdNameOutputDTO;
import antidimon.web.managervac.models.enums.TaskPriority;
import antidimon.web.managervac.models.enums.TaskStatus;
import lombok.*;

import java.sql.Timestamp;
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
    private Timestamp createdAt;
    private Timestamp deadline;
    private List<MyUserIdNameOutputDTO> developers;
    private List<CommentOutputDTO> comments;

}
