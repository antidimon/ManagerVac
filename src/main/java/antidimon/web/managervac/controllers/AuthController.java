package antidimon.web.managervac.controllers;


import antidimon.web.managervac.models.dto.auth.LoginRequest;
import antidimon.web.managervac.models.dto.user.MyUserInputDTO;
import antidimon.web.managervac.models.dto.user.MyUserOutputDTO;
import antidimon.web.managervac.security.jwt.JwtTokenUtil;
import antidimon.web.managervac.services.MyUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

@Tag(name = "Контроллер аутентификации", description = "Регистрация и аутентификация пользователя")
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Неверный запрос",
                content = @Content(mediaType = "text/plain",
                        examples = {@ExampleObject(value = "Invalid data")})),
        @ApiResponse(responseCode = "404", description = "Не найдено",
                content = @Content(mediaType = "text/plain",
                        examples = {@ExampleObject(value = "Object not found")})),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                content = @Content(mediaType = "text/plain",
                examples = {@ExampleObject(value = "Server error")}))
})
public class AuthController {

    private MyUserService myUserService;
    private JwtTokenUtil jwtTokenUtil;
    private AuthenticationManager authenticationManager;

    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован",
            content = @Content(mediaType = "application/json",
                    examples = {@ExampleObject(name = "Created", value = "{\n" +
                            "  \"id\": 1,\n" +
                            "  \"username\": \"Test\",\n" +
                            "  \"email\": \"example@mail.ru\",\n" +
                            "  \"createdAt\": \"2025-01-01T00:00:00.000\"\n" +
                            "}")}))
    @PostMapping("/register")
    public ResponseEntity<MyUserOutputDTO> registerUser(@RequestBody @Valid MyUserInputDTO myUserInputDTO) {

        MyUserOutputDTO myUserOutputDTO = myUserService.saveUser(myUserInputDTO);
        return new ResponseEntity<>(myUserOutputDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Аутентификация пользователя",
                description = "Аутентификация и возврат JWT токена пользователю")
    @ApiResponse(responseCode = "200", description = "Аутентификация успешна",
            content = @Content(mediaType = "application/json",
                    examples = {@ExampleObject(name = "Success",
                            value = " \"token\": \"nco3bublhbo38q5btq4wt3jbvqvtuiq\"" +
                    "}")}))
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());

        return ResponseEntity.ok("{\"token\":\"" + token + "\"}");
    }

}
