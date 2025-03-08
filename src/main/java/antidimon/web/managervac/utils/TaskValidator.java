package antidimon.web.managervac.utils;

import antidimon.web.managervac.models.dto.task.TaskInputDTO;
import antidimon.web.managervac.models.entities.Project;
import antidimon.web.managervac.models.entities.Task;
import antidimon.web.managervac.models.enums.TaskPriority;
import antidimon.web.managervac.models.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TaskValidator {


    public String validate(Task task, Project project) {

        StringBuilder errors = new StringBuilder();

        if (!isValidStatus(task.getStatus())){
            errors.append("Invalid status");
        }
        if (!isValidPriority(task.getPriority())){
            errors.append("Invalid priority");
        }
        if (!isValidTaskName(task.getTaskName(), project)){
            errors.append("Invalid task name");
        }

        return errors.toString();
    }

    public boolean isValidStatus(@NotNull TaskStatus status) {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.equals(status)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidPriority(@NotNull TaskPriority priority) {
        for (TaskPriority taskPriority : TaskPriority.values()) {
            if (taskPriority.equals(priority)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidTaskName(String taskName, Project project) {
        return project.getTasks().stream().noneMatch(task -> task.getTaskName().equals(taskName));
    }

    public String validateStatus(Task task) {
        if (!isValidStatus(task.getStatus())) return "Invalid status";
        return "";
    }
}
