package antidimon.web.managervac.unit.services;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import antidimon.web.managervac.mappers.MyUserMapper;
import antidimon.web.managervac.models.dto.user.MyUserInputDTO;
import antidimon.web.managervac.models.dto.user.MyUserOutputDTO;
import antidimon.web.managervac.models.entities.MyUser;
import antidimon.web.managervac.repositories.MyUserRepository;
import antidimon.web.managervac.services.MyUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class MyUserServiceTest {

    @Mock
    private MyUserRepository myUserRepository;

    @Mock
    private MyUserMapper myUserMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MyUserService myUserService;

    private MyUserInputDTO myUserInputDTO;
    private MyUser myUser;
    private MyUserOutputDTO myUserOutputDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        myUserInputDTO = new MyUserInputDTO();
        myUserInputDTO.setUsername("Test");
        myUserInputDTO.setEmail("example@mail.ru");
        myUserInputDTO.setPassword("password");

        myUser = new MyUser();
        myUser.setId(1L);
        myUser.setUsername("Test");
        myUser.setEmail("example@mail.ru");
        myUser.setPassword("encodedPassword");

        myUserOutputDTO = new MyUserOutputDTO();
        myUserOutputDTO.setId(1L);
        myUserOutputDTO.setUsername("Test");
        myUserOutputDTO.setEmail("example@mail.ru");
    }

    @Test
    public void testSaveUser() {
        when(myUserMapper.toEntity(any(MyUserInputDTO.class))).thenReturn(myUser);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(myUserRepository.save(any(MyUser.class))).thenReturn(myUser);
        when(myUserRepository.findById(any(Long.class))).thenReturn(Optional.of(myUser));
        when(myUserMapper.toOutputDTO(any(MyUser.class))).thenReturn(myUserOutputDTO);

        MyUserOutputDTO result = myUserService.saveUser(myUserInputDTO);

        assertNotNull(result);
        assertEquals(myUserOutputDTO.getId(), result.getId());
        assertEquals(myUserOutputDTO.getUsername(), result.getUsername());
        assertEquals(myUserOutputDTO.getEmail(), result.getEmail());

        verify(passwordEncoder).encode(myUser.getPassword());
        verify(myUserRepository).save(myUser);
    }

    @Test
    public void testGetExistingUser() {
        when(myUserRepository.findById(1L)).thenReturn(Optional.of(myUser));

        MyUser result = myUserService.getUser(1L);

        assertNotNull(result);
        assertEquals(myUser.getId(), result.getId());

        verify(myUserRepository).findById(1L);
    }

    @Test
    public void testGetNonExistingUser() {
        when(myUserRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            myUserService.getUser(1L);
        });

        assertEquals("User not found", exception.getMessage());

        verify(myUserRepository).findById(1L);
    }

    @Test
    public void testGetUsers() {
        when(myUserRepository.findAll()).thenReturn(List.of(myUser));

        List<MyUser> users = myUserService.getUsers();

        assertNotNull(users);
        assertEquals(1, users.size());

        verify(myUserRepository).findAll();
    }

    @Test
    public void testGetUsersDTO() {
        when(myUserRepository.findAll()).thenReturn(List.of(myUser));
        when(myUserMapper.toOutputDTO(any(MyUser.class))).thenReturn(myUserOutputDTO);

        List<MyUserOutputDTO> usersDto = myUserService.getUsersDTO();

        assertNotNull(usersDto);
        assertEquals(1, usersDto.size());

        verify(myUserMapper).toOutputDTO(any(MyUser.class));
    }
}

