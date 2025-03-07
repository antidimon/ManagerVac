package antidimon.web.managervac.models.dto.project;

import antidimon.web.managervac.models.dto.task.TaskOutputDTO;
import antidimon.web.managervac.models.dto.user.MyUserIdNameOutputDTO;
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
public class ProjectOutputDTO {

    private long id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createdAt;
    private List<MyUserIdNameOutputDTO> members;
    private List<TaskOutputDTO> tasks;
}
