package antidimon.web.managervac.controllers;


import antidimon.web.managervac.services.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@AllArgsConstructor
public class ProjectController {

    private ProjectService projectService;

}
