package antidimon.web.managervac.controllers;


import antidimon.web.managervac.models.dto.project.ProjectEditDTO;
import antidimon.web.managervac.models.dto.project.ProjectInputDTO;
import antidimon.web.managervac.models.dto.project.ProjectOutputDTO;
import antidimon.web.managervac.models.dto.user.MemberInputDTO;
import antidimon.web.managervac.security.jwt.JwtTokenUtil;
import antidimon.web.managervac.services.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/projects")
@AllArgsConstructor
public class ProjectController {

    private final JwtTokenUtil jwtTokenUtil;
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectOutputDTO>> getUserProjects(@RequestHeader("Authorization") String jwt) {
        long userId = jwtTokenUtil.getId(jwt);
        var list = projectService.getUserProjectsDTO(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectOutputDTO> getProject(@RequestHeader("Authorization") String jwt,
                                        @PathVariable long id) {
        long userId = jwtTokenUtil.getId(jwt);
        var project = projectService.getProjectDTO(id, userId);
        return ResponseEntity.ok(project);
    }

    @PostMapping
    public ResponseEntity<ProjectOutputDTO> createProject(@RequestHeader("Authorization") String jwt,
                                           @RequestBody @Valid ProjectInputDTO projectInputDTO) {
        long userId = jwtTokenUtil.getId(jwt);
        var project = projectService.createProject(projectInputDTO, userId);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@RequestHeader("Authorization") String jwt,
                                           @PathVariable("id") long projectId) {
        long userId = jwtTokenUtil.getId(jwt);
        projectService.deleteProject(userId, projectId);
        return ResponseEntity.ok().body("Deleted successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectOutputDTO> editProject(@RequestHeader("Authorization") String jwt,
                                         @RequestBody @Valid ProjectEditDTO projectEditDTO,
                                         @PathVariable long id){
        long userId = jwtTokenUtil.getId(jwt);
        var project = projectService.editProject(id, projectEditDTO.getName(), userId);
        return ResponseEntity.ok(project);
    }


    @PostMapping("/{id}/members")
    public ResponseEntity<String> addProjectMember(@RequestHeader("Authorization") String jwt,
                                              @RequestBody @Valid MemberInputDTO memberInputDTO,
                                              @PathVariable long id) {
        long senderId = jwtTokenUtil.getId(jwt);
        projectService.addMember(id, memberInputDTO.getUserId(), senderId);
        return ResponseEntity.ok().body("Added successfully");
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<String> kickProjectMember(@RequestHeader("Authorization") String jwt,
                                               @PathVariable long userId,
                                               @PathVariable long id){
        long senderId = jwtTokenUtil.getId(jwt);
        projectService.kickProjectMember(id, userId, senderId);
        return ResponseEntity.ok().body("Kicked successfully");
    }


}
