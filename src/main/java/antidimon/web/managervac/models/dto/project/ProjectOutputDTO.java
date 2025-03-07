package antidimon.web.managervac.models.dto.project;

import antidimon.web.managervac.models.dto.task.TaskOutputDTO;
import antidimon.web.managervac.models.dto.user.MyUserIdNameOutputDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectOutputDTO {

    private long id;
    private String name;
    private LocalDateTime createdAt;
    private List<MyUserIdNameOutputDTO> members;
    private List<TaskOutputDTO> tasks;
}
