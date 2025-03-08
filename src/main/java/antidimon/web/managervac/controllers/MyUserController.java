package antidimon.web.managervac.controllers;

import antidimon.web.managervac.models.dto.user.MyUserOutputDTO;
import antidimon.web.managervac.services.MyUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class MyUserController {

    private MyUserService myUserService;

    @GetMapping
    public ResponseEntity<List<MyUserOutputDTO>> getUser(@RequestHeader("Authorization") String jwt){
        var users = myUserService.getUsersDTO();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MyUserOutputDTO> getUserById(@PathVariable("id") long id){
        var user = myUserService.getUserDTO(id);
        return ResponseEntity.ok(user);
    }

}
