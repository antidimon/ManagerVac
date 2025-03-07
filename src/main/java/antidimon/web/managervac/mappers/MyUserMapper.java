package antidimon.web.managervac.mappers;

import antidimon.web.managervac.models.dto.user.MyUserInputDTO;
import antidimon.web.managervac.models.dto.user.MyUserOutputDTO;
import antidimon.web.managervac.models.entities.MyUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MyUserMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public MyUser toEnity(MyUserInputDTO myUserInputDTO) {
        return modelMapper.map(myUserInputDTO, MyUser.class);
    }

    public MyUserOutputDTO toOutputDTO(MyUser myUser) {
        return modelMapper.map(myUser, MyUserOutputDTO.class);
    }
}
