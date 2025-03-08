package antidimon.web.managervac.controllers;


import antidimon.web.managervac.models.dto.project.ProjectEditDTO;
import antidimon.web.managervac.models.dto.project.ProjectInputDTO;
import antidimon.web.managervac.models.dto.project.ProjectOutputDTO;
import antidimon.web.managervac.models.dto.user.MemberInputDTO;
import antidimon.web.managervac.security.jwt.JwtTokenUtil;
import antidimon.web.managervac.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Контроллер проектов", description = "CRUD операции над проектами + добавление/удаление участников")
@RestController
@RequestMapping("/api/projects")
@AllArgsConstructor
@SecurityRequirement(name = "JWT")
@ApiResponses (value = {
        @ApiResponse(responseCode = "400", description = "Неверный запрос"),
        @ApiResponse(responseCode = "401", description = "Не авторизован"),
        @ApiResponse(responseCode = "404", description = "Не найдено"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
})
public class ProjectController {

    private final JwtTokenUtil jwtTokenUtil;
    private ProjectService projectService;

    @Operation(summary = "Получение всех проектов пользователя")
    @ApiResponse(responseCode = "200", description = "Успешно получены проекты")
    @GetMapping
    public ResponseEntity<List<ProjectOutputDTO>> getUserProjects(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt) {

        long userId = jwtTokenUtil.getId(jwt);
        var list = projectService.getUserProjectsDTO(userId);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Получение проекта")
    @ApiResponse(responseCode = "200", description = "Успешно получен проект")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectOutputDTO> getProject(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("id") long projectId) {

        long userId = jwtTokenUtil.getId(jwt);
        var project = projectService.getProjectDTO(projectId, userId);
        return ResponseEntity.ok(project);
    }

    @Operation(summary = "Создание нового проекта")
    @ApiResponse(responseCode = "201", description = "Проект успешно создан")
    @PostMapping
    public ResponseEntity<ProjectOutputDTO> createProject(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @RequestBody @Valid ProjectInputDTO projectInputDTO) {

        long userId = jwtTokenUtil.getId(jwt);
        var project = projectService.createProject(projectInputDTO, userId);
        return ResponseEntity.ok(project);
    }

    @Operation(summary = "Удаление проекта")
    @ApiResponse(responseCode = "200", description = "Проект успешно удален")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("id") long projectId) {

        long userId = jwtTokenUtil.getId(jwt);
        projectService.deleteProject(userId, projectId);
        return ResponseEntity.ok().body("Deleted successfully");
    }

    @Operation(summary = "Изменение имени проекта",
            description = "Доступно только создателю проекта")
    @ApiResponse(responseCode = "200", description = "Имя проекта успешно изменено")
    @PatchMapping("/{id}")
    public ResponseEntity<ProjectOutputDTO> editProject(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @RequestBody @Valid ProjectEditDTO projectEditDTO,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("id") long projectId){

        long userId = jwtTokenUtil.getId(jwt);
        var project = projectService.editProject(projectId, projectEditDTO.getName(), userId);
        return ResponseEntity.ok(project);
    }

    @Operation(summary = "Добавление участника проекта",
            description = "Доступно только создателю проекта")
    @ApiResponse(responseCode = "200", description = "Участник успешно добавлен")
    @PostMapping("/{id}/members")
    public ResponseEntity<String> addProjectMember(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @RequestBody @Valid MemberInputDTO memberInputDTO,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("id") long projectId) {

        long senderId = jwtTokenUtil.getId(jwt);
        projectService.addMember(projectId, memberInputDTO.getUserId(), senderId);
        return ResponseEntity.ok().body("Added successfully");
    }

    @Operation(summary = "Удаление участника проекта",
            description = "Доступно только создателю проекта")
    @ApiResponse(responseCode = "200", description = "Участник успешно удалён")
    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<String> kickProjectMember(
            @Parameter(description = "JWT Token", required = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Идентификатор пользователя", required = true) @PathVariable long userId,
            @Parameter(description = "Идентификатор проекта", required = true) @PathVariable("id") long projectId){

        long senderId = jwtTokenUtil.getId(jwt);
        projectService.kickProjectMember(projectId, userId, senderId);
        return ResponseEntity.ok().body("Kicked successfully");
    }


}
