package antidimon.web.managervac.models.dto.project;

import antidimon.web.managervac.models.dto.task.TaskOutputDTO;
import antidimon.web.managervac.models.dto.user.MyUserIdNameOutputDTO;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProjectOutputDTO {

    private long id;
    private String name;
    private Timestamp createdAt;
    private List<MyUserIdNameOutputDTO> members;
    private List<TaskOutputDTO> tasks;
}
