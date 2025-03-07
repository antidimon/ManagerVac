package antidimon.web.managervac.services;

import antidimon.web.managervac.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;

}
