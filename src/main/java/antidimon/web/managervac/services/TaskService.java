package antidimon.web.managervac.services;

import antidimon.web.managervac.mappers.TaskMapper;
import antidimon.web.managervac.models.dto.task.TaskInputDTO;
import antidimon.web.managervac.models.dto.task.TaskOutputDTO;
import antidimon.web.managervac.models.entities.Project;
import antidimon.web.managervac.models.entities.Task;
import antidimon.web.managervac.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;
    private ProjectService projectService;
    private TaskMapper taskMapper;

    public List<Task> getAllProjectTasks(int projectId) {
        Project project = projectService.getProject(projectId);
        return project.getTasks();
    }

    public List<TaskOutputDTO> getAllProjectTasksDTO(int projectId, long senderId) throws NoSuchElementException, SecurityException {
        projectService.checkUserMemberOfProject(projectId, senderId);
        var tasks = this.getAllProjectTasks(projectId);
        return tasks.stream().map(taskMapper::toOutputDTO).toList();
    }

    public Task getProjectTask(int projectId, int taskId) throws NoSuchElementException, SecurityException {
        Optional<Task> foundedTask = this.getAllProjectTasks(projectId).stream()
                .filter(task -> task.getId() == taskId).findFirst();
        if (foundedTask.isPresent()) return foundedTask.get();
        throw new NoSuchElementException("Task not found");
    }

    public TaskOutputDTO getProjectTaskDTO(int projectId, int taskId, long senderId) throws NoSuchElementException, SecurityException {
        projectService.checkUserMemberOfProject(projectId, senderId);
        var task = this.getProjectTask(projectId, taskId);
        return taskMapper.toOutputDTO(task);
    }

    @Transactional
    public TaskOutputDTO createTask(int projectId, TaskInputDTO taskInputDTO, long senderId) throws SecurityException, NoSuchMethodException {
        projectService.checkUserOwnProject(projectId, senderId);
        Project project = projectService.getProject(projectId);
        Task task = taskMapper.toEntity(taskInputDTO);
        task.setProject(project);
        taskRepository.save(task);
        return taskMapper.toOutputDTO(task);
    }


}
