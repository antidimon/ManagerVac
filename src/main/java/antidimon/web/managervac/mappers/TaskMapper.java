package antidimon.web.managervac.mappers;

import antidimon.web.managervac.models.dto.task.TaskInputDTO;
import antidimon.web.managervac.models.dto.task.TaskOutputDTO;
import antidimon.web.managervac.models.entities.Task;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@AllArgsConstructor
public class TaskMapper {

    private final ModelMapper modelMapper = new ModelMapper();
    private MyUserMapper myUserMapper;
    private CommentMapper commentMapper;

    public Task toEntity(TaskInputDTO dto) {
        return modelMapper.map(dto, Task.class);
    }

    public TaskOutputDTO toOutputDTO(Task task) {
        return TaskOutputDTO.builder()
                .id(task.getId())
                .taskName(task.getTaskName())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .createdAt(task.getCreatedAt())
                .deadline(task.getDeadline())
                .developers((task.getDevelopers() == null) ? Collections.emptyList() :
                        task.getDevelopers().stream().map(myUserMapper::toIdNameOutputDTO).toList())
                .comments((task.getComments() == null) ? Collections.emptyList() :
                        task.getComments().stream().map(commentMapper::toOutputDTO).toList())
                .build();
    }
}
