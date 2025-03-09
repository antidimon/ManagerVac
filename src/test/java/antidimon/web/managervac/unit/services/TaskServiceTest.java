package antidimon.web.managervac.unit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import antidimon.web.managervac.mappers.TaskMapper;
import antidimon.web.managervac.models.dto.task.TaskEditDTO;
import antidimon.web.managervac.models.dto.task.TaskInputDTO;
import antidimon.web.managervac.models.dto.task.TaskOutputDTO;
import antidimon.web.managervac.models.entities.MyUser;
import antidimon.web.managervac.models.entities.Project;
import antidimon.web.managervac.models.entities.Task;
import antidimon.web.managervac.models.enums.TaskPriority;
import antidimon.web.managervac.models.enums.TaskStatus;
import antidimon.web.managervac.repositories.TaskRepository;
import antidimon.web.managervac.services.MyUserService;
import antidimon.web.managervac.services.ProjectService;
import antidimon.web.managervac.services.TaskService;
import antidimon.web.managervac.utils.TaskValidator;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private MyUserService myUserService;

    @Mock
    private TaskValidator taskValidator;

    @InjectMocks
    private TaskService taskService;

    private TaskInputDTO taskInputDTO;
    private TaskEditDTO taskEditDTO;
    private Task task;
    private Project project;
    private MyUser user;
    private TaskOutputDTO taskOutputDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        taskInputDTO = new TaskInputDTO();
        taskInputDTO.setTaskName("Test Task");
        taskInputDTO.setDescription("Test description");
        taskInputDTO.setStatus(TaskStatus.TODO);
        taskInputDTO.setPriority(TaskPriority.HIGH);
        taskInputDTO.setDeadline(LocalDateTime.now().plusDays(5));

        taskEditDTO = new TaskEditDTO();
        taskEditDTO.setTaskName("Updated Task");
        taskEditDTO.setDescription("Updated description");
        taskEditDTO.setStatus(TaskStatus.INPROGRESS);
        taskEditDTO.setPriority(TaskPriority.MEDIUM);
        taskEditDTO.setDeadline(LocalDateTime.now().plusDays(3));

        user = new MyUser();
        user.setId(1L);

        project = new Project();
        project.setId(1L);

        task = new Task();
        task.setId(1L);
        task.setTaskName("Test Task");
        task.setDescription("Test description");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setDeadline(LocalDateTime.now().plusDays(5));

        List<MyUser> developers = new ArrayList<>();
        developers.add(user);
        task.setDevelopers(developers);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        project.setTasks(tasks);

        taskOutputDTO = new TaskOutputDTO();
        taskOutputDTO.setId(1L);
        taskOutputDTO.setTaskName("Test Task");
    }

    @Test
    public void testGetAllProjectTasks() {
        when(projectService.getProject(1L)).thenReturn(project);

        List<Task> result = taskService.getAllProjectTasks(1L);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(projectService).getProject(1L);
    }

    @Test
    public void testGetAllProjectTasksDTO() {
        doNothing().when(projectService).checkUserMemberOfProject(any(Long.class), any(Long.class));
        when(projectService.getProject(any(Long.class))).thenReturn(project);
        when(taskMapper.toOutputDTO(any(Task.class))).thenReturn(taskOutputDTO);

        List<TaskOutputDTO> result = taskService.getAllProjectTasksDTO(1L, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(taskMapper).toOutputDTO(any(Task.class));
    }

    @Test
    public void testGetProjectTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getProjectTask(1L);

        assertNotNull(result);

        verify(taskRepository).findById(1L);
    }

    @Test
    public void testGetNonExistingProjectTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            taskService.getProjectTask(1L);
        });

        assertEquals("Task not found", exception.getMessage());

        verify(taskRepository).findById(1L);
    }

    @Test
    public void testCreateTask() {
        doNothing().when(projectService).checkUserOwnProject(any(Long.class), any(Long.class));
        when(projectService.getProject(any(Long.class))).thenReturn(project);
        when(taskMapper.toEntity(any(TaskInputDTO.class))).thenReturn(task);
        when(taskValidator.validate(any(Task.class), any(Project.class))).thenReturn("");
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toOutputDTO(any(Task.class))).thenReturn(taskOutputDTO);

        TaskOutputDTO result = taskService.createTask(1L, taskInputDTO, 1L);

        assertNotNull(result);
        assertEquals("Test Task", result.getTaskName());

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void testCreateTaskWithValidationError() {
        doNothing().when(projectService).checkUserOwnProject(any(Long.class), any(Long.class));
        when(projectService.getProject(any(Long.class))).thenReturn(project);
        when(taskMapper.toEntity(any(TaskInputDTO.class))).thenReturn(task);
        when(taskValidator.validate(any(Task.class), any(Project.class))).thenReturn("Validation error");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(1L, taskInputDTO, 1L);
        });

        assertEquals("Validation error", exception.getMessage());

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void testEditTask() throws BadRequestException {
        doNothing().when(projectService).checkUserMemberOfProject(any(Long.class), any(Long.class));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(projectService.getProjectOwnerId(any(Project.class))).thenReturn(user.getId());
        when(taskValidator.validate(any(Task.class), any(Project.class))).thenReturn("");
        when(taskValidator.validateStatus(any(Task.class))).thenReturn("");
        when(taskMapper.toOutputDTO(any(Task.class))).thenReturn(taskOutputDTO);

        TaskOutputDTO result = taskService.editTask(1L, 1L, taskEditDTO, 1L);

        assertNotNull(result);

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void testEditNonExistingTask() {
        doNothing().when(projectService).checkUserMemberOfProject(any(Long.class), any(Long.class));
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            taskService.editTask(1L, 1L, taskEditDTO, 1L);
        });

        assertEquals("Task not found", exception.getMessage());

        verify(taskRepository).findById(1L);
    }

    @Test
    public void testDeleteTask() {
        doNothing().when(projectService).checkUserOwnProject(any(Long.class), any(Long.class));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertDoesNotThrow(() -> {
            taskService.deleteTask(1L, 1L, 1L);
            verify(taskRepository).delete(any(Task.class));
        });
    }

    @Test
    public void testDeleteNonExistingTask() {
        doNothing().when(projectService).checkUserOwnProject(any(Long.class), any(Long.class));
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            taskService.deleteTask(1L, 1L, 1L);
        });

        assertEquals("Task not found", exception.getMessage());

        verify(taskRepository).findById(1L);
    }

    @Test
    public void testAddDeveloper() {
        doNothing().when(projectService).checkUserOwnProject(any(Long.class), any(Long.class));
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(task));
        when(myUserService.getUser(any(Long.class))).thenReturn(user);

        assertThrows(BadRequestException.class, () -> {
            taskService.addTaskDeveloper(1L, 1L, user.getId(), 1L);
            verify(taskRepository).save(any(Task.class));
        });
    }

    @Test
    public void testAddExistingDeveloper() {
        List<MyUser> developers = new ArrayList<>();
        developers.add(user);
        task.setDevelopers(developers);

        doNothing().when(projectService).checkUserOwnProject(any(Long.class), any(Long.class));
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(task));
        when(myUserService.getUser(any(Long.class))).thenReturn(user);

        Exception exception = assertThrows(BadRequestException.class, () -> {
            taskService.addTaskDeveloper(1L, 1L, user.getId(), 1L);
        });

        assertEquals("User already is a developer", exception.getMessage());

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void testKickDeveloper() {
        List<MyUser> developers = new ArrayList<>();
        developers.add(user);
        task.setDevelopers(developers);

        doNothing().when(projectService).checkUserOwnProject(any(Long.class), any(Long.class));
        when(myUserService.getUser(user.getId())).thenReturn(user);
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(task));

        assertDoesNotThrow(() -> {
            taskService.kickTaskDeveloper(1L, 1L, user.getId(), 1L);
            verify(taskRepository).save(any(Task.class));
        });
    }

    @Test
    public void testKickNonExistingDeveloper() {
        List<MyUser> developers = new ArrayList<>();
        project.setMembers(new ArrayList<>());
        task.setDevelopers(developers);

        doNothing().when(projectService).checkUserOwnProject(any(Long.class), any(Long.class));
        when(myUserService.getUser(user.getId())).thenReturn(user);
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(task));

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            taskService.kickTaskDeveloper(1L, 2L, user.getId(), 1L);
        });

        assertEquals("User is not a developer", exception.getMessage());

        verify(taskRepository, never()).save(any(Task.class));
    }
}