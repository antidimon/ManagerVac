package antidimon.web.managervac.services;

import antidimon.web.managervac.mappers.TaskMapper;
import antidimon.web.managervac.models.dto.task.TaskEditDTO;
import antidimon.web.managervac.models.dto.task.TaskInputDTO;
import antidimon.web.managervac.models.dto.task.TaskOutputDTO;
import antidimon.web.managervac.models.entities.MyUser;
import antidimon.web.managervac.models.entities.Project;
import antidimon.web.managervac.models.entities.Task;
import antidimon.web.managervac.repositories.TaskRepository;
import antidimon.web.managervac.utils.TaskValidator;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskValidator taskValidator;
    private TaskRepository taskRepository;
    private ProjectService projectService;
    private TaskMapper taskMapper;
    private MyUserService myUserService;

    public List<Task> getAllProjectTasks(long projectId) {
        Project project = projectService.getProject(projectId);
        return project.getTasks();
    }

    public List<TaskOutputDTO> getAllProjectTasksDTO(long projectId, long senderId)
            throws NoSuchElementException, SecurityException {
        projectService.checkUserMemberOfProject(projectId, senderId);
        var tasks = this.getAllProjectTasks(projectId);
        return tasks.stream().map(taskMapper::toOutputDTO).toList();
    }

    public Task getProjectTask(long taskId)
            throws NoSuchElementException, SecurityException {
        Optional<Task> foundedTask = taskRepository.findById(taskId);
        if (foundedTask.isPresent()) return foundedTask.get();
        throw new NoSuchElementException("Task not found");
    }

    public TaskOutputDTO getProjectTaskDTO(long projectId, long taskId, long senderId)
            throws NoSuchElementException, SecurityException {
        projectService.checkUserMemberOfProject(projectId, senderId);
        var task = this.getProjectTask(taskId);
        return taskMapper.toOutputDTO(task);
    }

    @Transactional
    public TaskOutputDTO createTask(long projectId, TaskInputDTO taskInputDTO, long senderId)
            throws SecurityException, NoSuchElementException, IllegalArgumentException {
        projectService.checkUserOwnProject(projectId, senderId);
        Project project = projectService.getProject(projectId);
        Task task = taskMapper.toEntity(taskInputDTO);
        String errors = taskValidator.validate(task, project);
        if (!errors.isEmpty()) throw new IllegalArgumentException(errors);
        task.setProject(project);
        taskRepository.save(task);
        return taskMapper.toOutputDTO(task);
    }

    @Transactional
    public TaskOutputDTO editTask(long projectId, long taskId, TaskEditDTO taskEditDTO, long senderId)
            throws NoSuchElementException, SecurityException, BadRequestException {
        projectService.checkUserMemberOfProject(projectId, senderId);
        Task task = this.getProjectTask(taskId);
        long ownerId = projectService.getProjectOwnerId(task.getProject());
        boolean isUserCanEditTask = task.getDevelopers().stream().anyMatch(dev -> dev.getId() == senderId) || ownerId == senderId;

        if (isUserCanEditTask){
            if (senderId != ownerId){
                if (taskEditDTO.getStatus() == null)
                    throw new BadRequestException("Developers can only update status or write comments");
                task.setStatus(taskEditDTO.getStatus());
                String errors = taskValidator.validateStatus(task);
                if (!errors.isEmpty()) throw new IllegalArgumentException(errors);
            }else {
                task.setTaskName((taskEditDTO.getTaskName() == null || taskEditDTO.getTaskName().isEmpty()) ?
                        task.getTaskName() : taskEditDTO.getTaskName());
                task.setStatus((taskEditDTO.getStatus() == null) ?
                        task.getStatus() : taskEditDTO.getStatus());
                task.setDescription((taskEditDTO.getDescription() == null || taskEditDTO.getDescription().isEmpty()) ?
                        task.getDescription() : taskEditDTO.getDescription());
                task.setDescription((taskEditDTO.getDescription() == null) ?
                        task.getDescription() : taskEditDTO.getDescription());
                task.setDeadline((taskEditDTO.getDeadline() == null) ?
                        task.getDeadline() : taskEditDTO.getDeadline());
                String errors = taskValidator.validate(task, task.getProject());
                if (!errors.isEmpty()) throw new IllegalArgumentException(errors);
            }
            taskRepository.save(task);
            return taskMapper.toOutputDTO(task);
        }else{
            throw new SecurityException("Permission denied");
        }


    }

    @Transactional
    public void deleteTask(long projectId, long taskId, long senderId)
            throws NoSuchElementException, SecurityException {
        projectService.checkUserOwnProject(projectId, senderId);
        Task task = this.getProjectTask(taskId);
        taskRepository.delete(task);
    }

    @Transactional
    public void addTaskDeveloper(long projectId, long taskId, long userId, long senderId)
            throws NoSuchElementException, SecurityException, BadRequestException {
        projectService.checkUserOwnProject(projectId, senderId);
        Task task = this.getProjectTask(taskId);
        MyUser user = myUserService.getUser(userId);

        if (task.getDevelopers().contains(user)) throw new BadRequestException("User already is a developer");

        task.getDevelopers().add(user);
        taskRepository.save(task);

    }

    @Transactional
    public void kickTaskDeveloper(long projectId, long id, long userId, long senderId)
            throws NoSuchElementException, SecurityException {
        projectService.checkUserOwnProject(projectId, senderId);
        Task task = this.getProjectTask(id);
        MyUser user = myUserService.getUser(userId);

        if (!task.getDevelopers().contains(user)) throw new NoSuchElementException("User is not a developer");

        task.getDevelopers().remove(user);
        taskRepository.save(task);
    }

    public void checkUserDevelopTask(long taskId, long userId) throws SecurityException {
        Task task = this.getProjectTask(taskId);
        boolean flag = task.getDevelopers().stream().anyMatch(dev -> dev.getId() == userId) ||
                projectService.getProjectOwnerId(task.getProject()) == userId;
        if (!flag) throw new SecurityException("Permission denied");
    }

    public Page<TaskOutputDTO> getUserTasks(long senderId, int page, int size, boolean isAsc, Long projectId) {
        Sort sort = isAsc ? Sort.by("deadline").ascending() : Sort.by("deadline").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Task> tasks;
        if (projectId != null) {
            tasks = taskRepository.findByUserIdAndProjectId(senderId, projectId, pageable);
        } else {
            tasks = taskRepository.findByUserId(senderId, pageable);
        }
        return tasks.map(taskMapper::toOutputDTO);
    }
}
