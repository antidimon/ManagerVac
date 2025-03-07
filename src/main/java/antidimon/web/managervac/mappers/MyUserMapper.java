package antidimon.web.managervac.mappers;

import antidimon.web.managervac.models.dto.user.MyUserIdNameOutputDTO;
import antidimon.web.managervac.models.dto.user.MyUserInputDTO;
import antidimon.web.managervac.models.dto.user.MyUserOutputDTO;
import antidimon.web.managervac.models.entities.MyUser;
import antidimon.web.managervac.models.entities.ProjectMember;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MyUserMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public MyUser toEntity(MyUserInputDTO myUserInputDTO) {
        return modelMapper.map(myUserInputDTO, MyUser.class);
    }

    public MyUserOutputDTO toOutputDTO(MyUser myUser) {
        return modelMapper.map(myUser, MyUserOutputDTO.class);
    }

    public MyUserIdNameOutputDTO toIdNameOutputDTO(ProjectMember projectMember) {

        return MyUserIdNameOutputDTO.builder()
                .id(projectMember.getUser().getId())
                .username(projectMember.getUser().getUsername())
                .build();
    }

    public MyUserIdNameOutputDTO toIdNameOutputDTO(MyUser myUser) {
        return MyUserIdNameOutputDTO.builder()
                .id(myUser.getId())
                .username(myUser.getUsername())
                .build();
    }
}
