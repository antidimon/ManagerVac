package antidimon.web.managervac.controllers;


import antidimon.web.managervac.models.dto.task.TaskEditDTO;
import antidimon.web.managervac.models.dto.task.TaskInputDTO;
import antidimon.web.managervac.models.dto.task.TaskOutputDTO;
import antidimon.web.managervac.models.dto.user.MemberInputDTO;
import antidimon.web.managervac.security.jwt.JwtTokenUtil;
import antidimon.web.managervac.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Контроллер задач",
        description = "CRUD операции над задачами + добавление/удаление исполнителей")
@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
@AllArgsConstructor
@SecurityRequirement(name = "JWT")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Неверный запрос",
                content = @Content(mediaType = "text/plain",
                        examples = {@ExampleObject(value = "Invalid data")})),
        @ApiResponse(responseCode = "401", description = "Не авторизован",
                content = @Content(mediaType = "text/plain",
                        examples = {@ExampleObject(value = "Permission denied")})),
        @ApiResponse(responseCode = "404", description = "Не найдено",
                content = @Content(mediaType = "text/plain",
                        examples = {@ExampleObject(value = "Object not found")})),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                content = @Content(mediaType = "text/plain",
                        examples = {@ExampleObject(value = "Server error")}))
})
public class TaskController {

    private TaskService taskService;
    private JwtTokenUtil jwtTokenUtil;

    @Operation(summary = "Получение всех задач проекта",
            description = "Доступно только участникам проекта")
    @ApiResponse(responseCode = "200", description = "Успешно получены задачи",
            content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success", value = "[\n" +
                            "  {\n" +
                            "    \"id\": 1,\n" +
                            "    \"taskName\": \"Test Task\",\n" +
                            "    \"description\": \"This is a test task.\",\n" +
                            "    \"status\": \"TODO\",\n" +
                            "    \"priority\": \"HIGH\",\n" +
                            "    \"createdAt\": \"2025-01-01 00:00:00.000\",\n" +
                            "    \"deadline\": \"2025-01-10 00:00:00.000\",\n" +
                            "    \"developers\": [\n" +
                            "      {\n" +
                            "        \"id\": 1,\n" +
                            "        \"username\": \"Test\"\n" +
                            "      }\n" +
                            "    ],\n" +
                            "    \"comments\": []\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"id\": 2,\n" +
                            "    \"taskName\": \"Another Task\",\n" +
                            "    \"description\": \"This is another task.\",\n" +
                            "    \"status\": \"IN_PROGRESS\",\n" +
                            "    \"priority\": \"MEDIUM\",\n" +
                            "    \"createdAt\": \"2025-01-02 00:00:00.000\",\n" +
                            "    \"deadline\": \"2025-01-15 00:00:00.000\",\n" +
                            "    \"developers\": [],\n" +
                            "    \"comments\": []\n" +
                            "  }\n" +
                            "]")
            }))
    @GetMapping
    public ResponseEntity<List<TaskOutputDTO>> getAllProjectTasks(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("projectId") long projectId) {

        long senderId = jwtTokenUtil.getId(jwt);
        var tasks = taskService.getAllProjectTasksDTO(projectId, senderId);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Получение задачи",
            description = "Доступно только участникам проекта")
    @ApiResponse(responseCode = "200", description = "Успешно получена задача",
            content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success", value = "{\n" +
                            "\"id\": 1,\n" +
                            "\"taskName\": \"Test Task\",\n" +
                            "\"description\": \"This is a test task.\",\n" +
                            "\"status\": \"TODO\",\n" +
                            "\"priority\": \"HIGH\",\n" +
                            "\"createdAt\": \"2025-01-01 00:00:00.000\",\n" +
                            "\"deadline\": \"2025-01-10 00:00:00.000\",\n" +
                            "\"developers\": [\n" +
                            "{\n" +
                            "\"id\": 1,\n" +
                            "\"username\": \"Test\"\n" +
                            "}],\n"+
                            "\"comments\": []\n"+
                            "}"),
            }))
    @GetMapping("/{id}")
    public ResponseEntity<TaskOutputDTO> getTask(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("projectId") long projectId,
            @Parameter(description = "Идентификатор задачи", required = true) @PathVariable("id") long taskId) {

        long senderId = jwtTokenUtil.getId(jwt);
        var task = taskService.getProjectTaskDTO(projectId, taskId, senderId);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Создание новой задачи",
            description = "Доступно только создателю проекта")
    @ApiResponse(responseCode = "201", description = "Задача успешно создана",
            content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name="Created", value="{\n"+
                            "\"id\": 3,\n"+
                            "\"taskName\":\"New Task\",\n"+
                            "\"description\":\"This is a new task.\",\n"+
                            "\"status\":\"TODO\",\n"+
                            "\"priority\":\"LOW\",\n"+
                            "\"createdAt\":\"2025-01-03 00:00:00.000\",\n"+
                            "\"deadline\":\"2025-01-20 00:00:00.000\",\n"+
                            "\"developers\":[ ] , \n"+
                            "\"comments\":[ ] \n"+
                            "}"),
            }))
    @PostMapping
    public ResponseEntity<TaskOutputDTO> createTask(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @RequestBody TaskInputDTO taskInputDTO,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("projectId") long projectId) {

        long senderId = jwtTokenUtil.getId(jwt);
        var task = taskService.createTask(projectId, taskInputDTO, senderId);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @Operation(summary = "Изменение задачи",
            description = "Доступно только создателю проекта и исполнителям")
    @ApiResponse(responseCode = "200", description = "Задача успешно изменена",
            content = @Content(mediaType="application/json", examples={
                    @ExampleObject(name="Success", value="{\n"+
                            "\"id\": 1,\n"+
                            "\"taskName\":\"Updated Task Name\",\n"+
                            "\"description\":\"Updated description for the task.\",\n"+
                            "\"status\":\"IN_PROGRESS\",\n"+
                            "\"priority\":\"MEDIUM\",\n"+
                            "\"createdAt\":\"2025-01-01 00:00:00.000\",\n"+
                            "\"deadline\":\"2025-01-15 00:00:00.000\",\n"+
                            "\"developers\":[ ] , \n"+
                            "\"comments\":[ ] \n"+
                            "}"),
            }))
    @PatchMapping("/{id}")
    public ResponseEntity<TaskOutputDTO> editTask(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @RequestBody TaskEditDTO taskEditDTO,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("projectId") long projectId,
            @Parameter(description = "Идентификатор задачи", required = true) @PathVariable("id") long taskId) throws BadRequestException {

        long senderId = jwtTokenUtil.getId(jwt);
        var task = taskService.editTask(projectId, taskId, taskEditDTO, senderId);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Удаление задачи",
            description = "Доступно только создателю проекта")
    @ApiResponse(responseCode="200", description="Задача успешно удалена",
            content=@Content(mediaType="text/plain", examples={
                    @ExampleObject(name="Success", value="Deleted successfully")
            }))
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("projectId") long projectId,
            @Parameter(description = "Идентификатор задачи", required = true) @PathVariable("id") long taskId){

        long senderId = jwtTokenUtil.getId(jwt);
        taskService.deleteTask(projectId, taskId, senderId);
        return ResponseEntity.ok("Deleted successfully");
    }

    @Operation(summary = "Добавление исполнителя задачи",
            description = "Доступно только создателю проекта")
    @ApiResponse(responseCode="200", description="Исполнитель успешно добавлен",
            content=@Content(mediaType="text/plain", examples={
                    @ExampleObject(name="Success", value="Added successfully")
            }))
    @PostMapping("/{id}/developers")
    public ResponseEntity<String> addTaskDeveloper(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @RequestBody @Valid MemberInputDTO memberInputDTO,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("projectId") long projectId,
            @Parameter(description = "Идентификатор задачи", required = true) @PathVariable("id") long taskId) throws BadRequestException {

        long senderId = jwtTokenUtil.getId(jwt);
        taskService.addTaskDeveloper(projectId, taskId, memberInputDTO.getUserId(), senderId);
        return ResponseEntity.ok("Added successfully");
    }

    @Operation(summary = "Удаление исполнителя задачи",
            description = "Доступно только создателю проекта")
    @ApiResponse(responseCode="200", description="Исполнитель успешно удалён",
            content=@Content(mediaType="text/plain", examples= {
                    @ExampleObject(name = "Success", value = "Kicked successfully")
            }))
    @DeleteMapping("/{id}/developers/{userId}")
    public ResponseEntity<String> kickTaskDeveloper(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("projectId") long projectId,
            @Parameter(description = "Идентификатор задачи", required = true) @PathVariable("id") long taskId,
            @Parameter(description = "Идентификатор исполнителя") @PathVariable long userId){

        long senderId = jwtTokenUtil.getId(jwt);
        taskService.kickTaskDeveloper(projectId, taskId, userId, senderId);
        return ResponseEntity.ok().body("Kicked successfully");
    }
}
