package antidimon.web.managervac.controllers;


import antidimon.web.managervac.models.dto.task.TaskEditDTO;
import antidimon.web.managervac.models.dto.task.TaskInputDTO;
import antidimon.web.managervac.models.dto.task.TaskOutputDTO;
import antidimon.web.managervac.models.dto.user.MemberInputDTO;
import antidimon.web.managervac.security.jwt.JwtTokenUtil;
import antidimon.web.managervac.services.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
@AllArgsConstructor
public class TaskController {

    private TaskService taskService;
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping
    public ResponseEntity<List<TaskOutputDTO>> getAllProjectTasks(@RequestHeader("Authorization") String jwt,
                                                                  @PathVariable long projectId) {
        long senderId = jwtTokenUtil.getId(jwt);
        var tasks = taskService.getAllProjectTasksDTO(projectId, senderId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskOutputDTO> getProjectTask(@RequestHeader("Authorization") String jwt,
                                            @PathVariable long projectId,
                                            @PathVariable long id) {
        long senderId = jwtTokenUtil.getId(jwt);
        var task = taskService.getProjectTaskDTO(projectId, id, senderId);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<TaskOutputDTO> createTask(@RequestHeader("Authorization") String jwt,
                                        @RequestBody TaskInputDTO taskInputDTO,
                                        @PathVariable long projectId) {
        long senderId = jwtTokenUtil.getId(jwt);
        var task = taskService.createTask(projectId, taskInputDTO, senderId);
        return ResponseEntity.ok(task);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<TaskOutputDTO> editTask(@RequestHeader("Authorization") String jwt,
                                                  @RequestBody TaskEditDTO taskEditDTO,
                                                  @PathVariable long projectId,
                                                  @PathVariable long id) throws BadRequestException {
        long senderId = jwtTokenUtil.getId(jwt);
        var task = taskService.editTask(id, taskEditDTO, senderId);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@RequestHeader("Authorization") String jwt,
                                             @PathVariable long projectId,
                                             @PathVariable long id){
        long senderId = jwtTokenUtil.getId(jwt);
        taskService.deleteTask(projectId, id, senderId);
        return ResponseEntity.ok("Deleted successfully");
    }

    @PostMapping("/{id}/developers")
    public ResponseEntity<String> addTaskDeveloper(@RequestHeader("Authorization") String jwt,
                                                   @RequestBody @Valid MemberInputDTO memberInputDTO,
                                                   @PathVariable long projectId,
                                                   @PathVariable long id) throws BadRequestException {
        long senderId = jwtTokenUtil.getId(jwt);
        taskService.addTaskDeveloper(projectId, id, memberInputDTO.getUserId(), senderId);
        return ResponseEntity.ok("Added successfully");
    }

    @DeleteMapping("/{id}/developers/{userId}")
    public ResponseEntity<String> kickTaskDeveloper(@RequestHeader("Authorization") String jwt,
                                                   @PathVariable long projectId,
                                                   @PathVariable long id,
                                                    @PathVariable long userId){
        long senderId = jwtTokenUtil.getId(jwt);
        taskService.kickTaskDeveloper(projectId, id, userId, senderId);
        return ResponseEntity.ok().body("Kicked successfully");
    }
}
