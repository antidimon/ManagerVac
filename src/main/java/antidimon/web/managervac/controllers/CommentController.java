package antidimon.web.managervac.controllers;


import antidimon.web.managervac.models.dto.comment.CommentInputDTO;
import antidimon.web.managervac.models.dto.comment.CommentOutputDTO;
import antidimon.web.managervac.security.jwt.JwtTokenUtil;
import antidimon.web.managervac.services.CommentService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Контроллер комментариев", description = "CRUD операции над комментариями")
@RestController
@RequestMapping("/api/projects/{projectId}/tasks/{taskId}/comments")
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
public class CommentController {

    private CommentService commentService;
    private JwtTokenUtil jwtTokenUtil;

    @Operation(summary = "Получение всех комментариев по задаче",
            description = "Доступно только участникам проекта")
    @ApiResponse(responseCode = "200", description = "Успешно получены комментарии",
            content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success", value = "[\n" +
                            "  {\n" +
                            "    \"id\": 1,\n" +
                            "    \"user\": {\n" +
                            "      \"id\": 1,\n" +
                            "      \"username\": \"Test\"\n" +
                            "    },\n" +
                            "    \"comment\": \"Hello\",\n" +
                            "    \"createdAt\": \"2025-01-01 00:00:00.000\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"id\": 2,\n" +
                            "    \"user\": {\n" +
                            "      \"id\": 2,\n" +
                            "      \"username\": \"User2\"\n" +
                            "    },\n" +
                            "    \"comment\": \"Another comment\",\n" +
                            "    \"createdAt\": \"2025-01-01 01:00:00.000\"\n" +
                            "  }\n" +
                            "]")
            }))
    @GetMapping
    public ResponseEntity<List<CommentOutputDTO>> getTaskComments(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("projectId") long projectId,
            @Parameter(description = "Идентификатор задачи", required = true) @PathVariable("taskId") long taskId) {

        long senderId = jwtTokenUtil.getId(jwt);
        var comments = commentService.getTaskCommentsDTO(projectId, taskId, senderId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Получение комментария",
            description = "Доступно только участникам проекта")
    @ApiResponse(responseCode = "200", description = "Успешно получен комментарий",
            content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success", value = "{\n" +
                            "  \"id\": 1,\n" +
                            "  \"user\": {\n" +
                            "      \"id\": 1,\n" +
                            "      \"username\": \"Test\"\n" +
                            "  },\n" +
                            "  \"comment\": \"Hello\",\n" +
                            "  \"createdAt\": \"2025-01-01 00:00:00.000\"\n" +
                            "}"),
            }))
    @GetMapping("/{id}")
    public ResponseEntity<CommentOutputDTO> getComment(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("projectId") long projectId,
            @Parameter(description = "Идентификатор сообщения", required = true) @PathVariable("id") long commentId){

        long senderId = jwtTokenUtil.getId(jwt);
        var comment = commentService.getCommentDTO(projectId, commentId, senderId);
        return ResponseEntity.ok(comment);
    }

    @Operation(summary = "Создание нового комментария",
            description = "Доступно только для создателя проекта и автора комментария")
    @ApiResponse(responseCode = "201", description = "Комментарий успешно создан",
            content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name="Created", value="{\n" +
                            "\"id\": 3,\n"+
                            "\"user\": {\n"+
                            "\"id\": 1,\n"+
                            "\"username\": \"Test\"\n"+
                            "},\n"+
                            "\"comment\": \"New comment created\",\n"+
                            "\"createdAt\": \"2025-01-01 02:00:00.000\"\n"+
                            "}"),
            }))
    @PostMapping
    public ResponseEntity<CommentOutputDTO> createComment(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @RequestBody @Valid CommentInputDTO commentInputDTO,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("projectId") long projectId,
            @Parameter(description = "Идентификатор задачи", required = true) @PathVariable("taskId") long taskId){

        long senderId = jwtTokenUtil.getId(jwt);
        var comment = commentService.createComment(projectId, taskId, commentInputDTO, senderId);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @Operation(summary = "Изменение комментария",
            description = "Доступно только автору комментария")
    @ApiResponse(responseCode="200", description="Комментарий успешно изменён",
            content=@Content(mediaType="application/json", examples={
                    @ExampleObject(name="Success", value="{\n"+
                            "\"id\": 1,\n"+
                            "\"user\": {\n"+
                            "\"id\": 1,\n"+
                            "\"username\": \"Test\"\n"+
                            "},\n"+
                            "\"comment\":\"Updated comment text\",\n"+
                            "\"createdAt\":\"2025-01-01 00:00:00.000\"\n"+
                            "}"),
            }))
    @PatchMapping("/{id}")
    public ResponseEntity<CommentOutputDTO> editComment(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @RequestBody @Valid CommentInputDTO commentInputDTO,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("projectId") long projectId,
            @Parameter(description = "Идентификатор задачи", required = true) @PathVariable("taskId") long taskId,
            @Parameter(description = "Идентификатор сообщения", required = true) @PathVariable("id") long commentId){

        long senderId = jwtTokenUtil.getId(jwt);
        var comment = commentService.editComment(projectId, taskId, commentId, commentInputDTO.getComment(), senderId);
        return ResponseEntity.ok(comment);
    }

    @Operation(summary = "Удаление комментария",
            description = "Доступно только для создателя проекта и автора комментария")
    @ApiResponse(responseCode="200", description="Комментарий успешно удалён",
            content=@Content(mediaType="text/plain", examples={
                    @ExampleObject(name="Success", value="Deleted successfully")
            }))
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("projectId") long projectId,
            @Parameter(description = "Идентификатор задачи", required = true) @PathVariable("taskId") long taskId,
            @Parameter(description = "Идентификатор сообщения", required = true) @PathVariable("id") long commentId){

        long senderId = jwtTokenUtil.getId(jwt);
        commentService.deleteComment(projectId, taskId, commentId, senderId);
        return ResponseEntity.ok("Deleted successfully");
    }


}
