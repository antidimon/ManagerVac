package antidimon.web.managervac.controllers;

import antidimon.web.managervac.models.dto.task.TaskOutputDTO;
import antidimon.web.managervac.models.dto.user.MyUserOutputDTO;
import antidimon.web.managervac.security.jwt.JwtTokenUtil;
import antidimon.web.managervac.services.MyUserService;
import antidimon.web.managervac.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Контроллер пользователя",
        description = "Позволяет получать пользователей и их задачи с фильтрацией/пагинацией")
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@SecurityRequirement(name = "JWT")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Неверный запрос"),
        @ApiResponse(responseCode = "401", description = "Не авторизован"),
        @ApiResponse(responseCode = "404", description = "Не найдено"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
})
public class MyUserController {

    private JwtTokenUtil jwtTokenUtil;
    private TaskService taskService;
    private MyUserService myUserService;

    @Operation(summary = "Получение всех пользователей")
    @ApiResponse(responseCode = "200", description = "Успешно получены пользователи")
    @GetMapping
    public ResponseEntity<List<MyUserOutputDTO>> getUsers(){
        var users = myUserService.getUsersDTO();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Получение определённого пользователя")
    @ApiResponse(responseCode = "200", description = "Успешно получен пользователь")
    @GetMapping("/{id}")
    public ResponseEntity<MyUserOutputDTO> getUserById(
            @Parameter(description = "Идентификатор пользователя", required = true) @PathVariable("id") long id){
        var user = myUserService.getUserDTO(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Получение задач пользователя",
                description = "Доступна пагинация и фильтрация. Фильтрация по дедлайнам, по проекту")
    @ApiResponse(responseCode = "200", description = "Успешно получены задачи")
    @GetMapping("/tasks")
    public ResponseEntity<Page<TaskOutputDTO>> getUserTasks(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Идентификатор проекта для фильтрации") @RequestParam(name = "project", required = false) Long projectId,
            @Parameter(description = "Boolean flag для фильтрации по дедлайнам (true - по возрастанию)") @RequestParam(name = "deadlineSort", defaultValue = "true") boolean isAsc,
            @Parameter(description = "Страница с задачами") @RequestParam(name = "page", defaultValue = "") @Valid @Min(0) int page,
            @Parameter(description = "Количество задач на странице") @RequestParam(name = "size", defaultValue = "10") @Valid @Min(1) int size){
        long senderId = jwtTokenUtil.getId(jwt);
        var tasks = taskService.getUserTasks(senderId, page, size, isAsc, projectId);
        return ResponseEntity.ok(tasks);
    }



}
