package antidimon.web.managervac.services;

import antidimon.web.managervac.mappers.MyUserMapper;
import antidimon.web.managervac.models.dto.user.MyUserInputDTO;
import antidimon.web.managervac.models.dto.user.MyUserOutputDTO;
import antidimon.web.managervac.models.entities.MyUser;
import antidimon.web.managervac.repositories.MyUserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserService {

    private MyUserRepository myUserRepository;
    private MyUserMapper myUserMapper;
    private PasswordEncoder passwordEncoder;

    public MyUserOutputDTO saveUser(@Valid MyUserInputDTO myUserInputDTO) {
        MyUser user = myUserMapper.toEntity(myUserInputDTO);
        this.encodeUserPassword(user);
        myUserRepository.save(user);
        MyUser updatedUser = myUserRepository.findById(user.getId()).get();
        return myUserMapper.toOutputDTO(updatedUser);
    }



    private void encodeUserPassword(MyUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    public MyUser getUser(long userId) {
        return myUserRepository.findById(userId).get();
    }
}
