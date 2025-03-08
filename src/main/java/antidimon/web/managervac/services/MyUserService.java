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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MyUserService {

    private MyUserRepository myUserRepository;
    private MyUserMapper myUserMapper;
    private PasswordEncoder passwordEncoder;

    @Transactional
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

    public MyUser getUser(long userId) throws NoSuchElementException {
        Optional<MyUser> user = myUserRepository.findById(userId);
        if (user.isPresent()) return user.get();
        throw new NoSuchElementException("User not found");
    }

    public MyUserOutputDTO getUserDTO(long userId) throws NoSuchElementException {
        MyUser user = this.getUser(userId);
        return myUserMapper.toOutputDTO(user);
    }

    public List<MyUser> getUsers(){
        return myUserRepository.findAll();
    }

    public List<MyUserOutputDTO> getUsersDTO() {
        var users = this.getUsers();
        return users.stream().map(myUserMapper::toOutputDTO).toList();
    }
}
