package antidimon.web.managervac.controllers;


import antidimon.web.managervac.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private TaskService taskService;

}
