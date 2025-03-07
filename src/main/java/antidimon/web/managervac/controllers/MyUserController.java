package antidimon.web.managervac.controllers;

import antidimon.web.managervac.services.MyUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class MyUserController {

    private MyUserService myUserService;

}
