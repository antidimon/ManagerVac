package antidimon.web.managervac.mappers;

import antidimon.web.managervac.models.dto.project.ProjectInputDTO;
import antidimon.web.managervac.models.dto.project.ProjectOutputDTO;
import antidimon.web.managervac.models.entities.Project;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@AllArgsConstructor
public class ProjectMapper {

    private final ModelMapper modelMapper = new ModelMapper();
    private TaskMapper taskMapper;
    private MyUserMapper myUserMapper;

    public Project toEntity(ProjectInputDTO projectInputDTO){
        return modelMapper.map(projectInputDTO, Project.class);
    }

    public ProjectOutputDTO toOutputDTO(Project project) {

        return ProjectOutputDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .createdAt(project.getCreatedAt())
                .tasks((project.getTasks()==null) ? Collections.emptyList() :
                        project.getTasks().stream().map(taskMapper::toOutputDTO).toList())
                .members((project.getMembers()==null) ? Collections.emptyList() :
                        project.getMembers().stream().map(myUserMapper::toIdNameOutputDTO).toList())
                .build();
    }
}
