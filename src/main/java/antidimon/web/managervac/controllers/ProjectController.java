package antidimon.web.managervac.controllers;


import antidimon.web.managervac.models.dto.project.ProjectEditDTO;
import antidimon.web.managervac.models.dto.project.ProjectInputDTO;
import antidimon.web.managervac.models.dto.project.ProjectOutputDTO;
import antidimon.web.managervac.models.dto.projectMember.ProjectMemberInputDTO;
import antidimon.web.managervac.security.jwt.JwtTokenUtil;
import antidimon.web.managervac.services.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/projects")
@AllArgsConstructor
public class ProjectController {

    private final JwtTokenUtil jwtTokenUtil;
    private ProjectService projectService;


    @GetMapping
    public ResponseEntity<?> getUserProjects(@RequestHeader("Authorization") String jwt) {
        List<ProjectOutputDTO> list;
        try {
            long userId = jwtTokenUtil.getId(jwt);
            list = projectService.getUserProjectsDTO(userId);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProject(@RequestHeader("Authorization") String jwt,
                                        @PathVariable long id) {
        ProjectOutputDTO project;
        try {
            long userId = jwtTokenUtil.getId(jwt);
            project = projectService.getProjectDTO(id, userId);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.badRequest().body(noSuchElementException.getMessage());
        }catch (SecurityException securityException){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(securityException.getMessage());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(project);
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestHeader("Authorization") String jwt,
                                                          @RequestBody @Valid ProjectInputDTO projectInputDTO) {
        ProjectOutputDTO project;
        try {
            long userId = jwtTokenUtil.getId(jwt);
            project = projectService.createProject(projectInputDTO, userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@RequestHeader("Authorization") String jwt,
                                           @PathVariable("id") long projectId) {
        try {
            long userId = jwtTokenUtil.getId(jwt);
            projectService.deleteProject(userId, projectId);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.badRequest().body(noSuchElementException.getMessage());
        }catch (SecurityException securityException){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(securityException.getMessage());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok().body("Deleted successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editProject(@RequestHeader("Authorization") String jwt,
                                         @RequestBody @Valid ProjectEditDTO projectEditDTO,
                                         @PathVariable long id){

        try {
            long userId = jwtTokenUtil.getId(jwt);
            projectService.editProject(id, projectEditDTO.getName(), userId);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.badRequest().body(noSuchElementException.getMessage());
        }catch (SecurityException securityException){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(securityException.getMessage());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok().body("Edited successfully");
    }


    @PostMapping("/{id}/members")
    public ResponseEntity<?> addProjectMember(@RequestHeader("Authorization") String jwt,
                                              @RequestBody @Valid ProjectMemberInputDTO projectMemberInputDTO,
                                              @PathVariable long id) {

        try {
            long senderId = jwtTokenUtil.getId(jwt);
            projectService.addMember(id, projectMemberInputDTO.getUserId(), senderId);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.badRequest().body(noSuchElementException.getMessage());
        }catch (SecurityException securityException){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(securityException.getMessage());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok().body("Added successfully");
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<?> kickProjectMember(@RequestHeader("Authorization") String jwt,
                                               @PathVariable long userId,
                                               @PathVariable long id){

        try {
            long senderId = jwtTokenUtil.getId(jwt);
            projectService.kickProjectMember(id, userId, senderId);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.badRequest().body(noSuchElementException.getMessage());
        }catch (SecurityException securityException){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(securityException.getMessage());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok().body("Kicked successfully");
    }


}
