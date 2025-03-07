package antidimon.web.managervac.controllers;


import antidimon.web.managervac.models.dto.auth.LoginRequest;
import antidimon.web.managervac.models.dto.user.MyUserInputDTO;
import antidimon.web.managervac.models.dto.user.MyUserOutputDTO;
import antidimon.web.managervac.security.jwt.JwtTokenUtil;
import antidimon.web.managervac.services.MyUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private MyUserService myUserService;
    private JwtTokenUtil jwtTokenUtil;
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<MyUserOutputDTO> registerUser(@RequestBody @Valid MyUserInputDTO myUserInputDTO) {

        MyUserOutputDTO myUserOutputDTO = myUserService.saveUser(myUserInputDTO);
        return ResponseEntity.ok(myUserOutputDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());

        return ResponseEntity.ok("{\"token\":\"" + token + "\"}");
    }

}
