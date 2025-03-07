package antidimon.web.managervac.controllers;


import antidimon.web.managervac.models.dto.task.TaskInputDTO;
import antidimon.web.managervac.models.dto.task.TaskOutputDTO;
import antidimon.web.managervac.security.jwt.JwtTokenUtil;
import antidimon.web.managervac.services.ProjectService;
import antidimon.web.managervac.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
@AllArgsConstructor
public class TaskController {

    private TaskService taskService;
    private JwtTokenUtil jwtTokenUtil;
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<?> getAllProjectTasks(@RequestHeader("Authorization") String jwt, @PathVariable int projectId) {
        List<TaskOutputDTO> tasks;
        try {
            long senderId = jwtTokenUtil.getId(jwt);
            tasks = taskService.getAllProjectTasksDTO(projectId, senderId);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.badRequest().body(noSuchElementException.getMessage());
        }catch (SecurityException securityException){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(securityException.getMessage());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectTask(@RequestHeader("Authorization") String jwt,
                                            @PathVariable int projectId,
                                            @PathVariable int id) {

        TaskOutputDTO task;
        try {
            long senderId = jwtTokenUtil.getId(jwt);
            task = taskService.getProjectTaskDTO(projectId, id, senderId);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.badRequest().body(noSuchElementException.getMessage());
        }catch (SecurityException securityException){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(securityException.getMessage());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestHeader("Authorization") String jwt,
                                        @RequestBody TaskInputDTO taskInputDTO,
                                        @PathVariable int projectId) {

        TaskOutputDTO taskOutputDTO;
        try {
            long senderId = jwtTokenUtil.getId(jwt);
            taskOutputDTO = taskService.createTask(projectId, taskInputDTO, senderId);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.badRequest().body(noSuchElementException.getMessage());
        }catch (SecurityException securityException){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(securityException.getMessage());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(taskOutputDTO);

    }

}
