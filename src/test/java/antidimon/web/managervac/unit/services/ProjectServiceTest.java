package antidimon.web.managervac.unit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import antidimon.web.managervac.mappers.ProjectMapper;
import antidimon.web.managervac.models.dto.project.ProjectInputDTO;
import antidimon.web.managervac.models.dto.project.ProjectOutputDTO;
import antidimon.web.managervac.models.entities.MyUser;
import antidimon.web.managervac.models.entities.Project;
import antidimon.web.managervac.models.entities.ProjectMember;
import antidimon.web.managervac.models.entities.ProjectMemberKey;
import antidimon.web.managervac.models.enums.Role;
import antidimon.web.managervac.repositories.ProjectMemberRepository;
import antidimon.web.managervac.repositories.ProjectRepository;
import antidimon.web.managervac.services.MyUserService;
import antidimon.web.managervac.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private MyUserService myUserService;

    @InjectMocks
    private ProjectService projectService;

    private ProjectInputDTO projectInputDTO;
    private Project project;
    private ProjectOutputDTO projectOutputDTO;
    private MyUser user;
    private MyUser user2;
    private ProjectOutputDTO updatedProjectOutputDTO;

    @BeforeEach
    public void setUp() throws CloneNotSupportedException {
        MockitoAnnotations.openMocks(this);

        projectInputDTO = new ProjectInputDTO();
        projectInputDTO.setName("Test Project");

        user = new MyUser();
        user.setId(1L);
        user.setUsername("Test");

        user2 = new MyUser();
        user2.setId(2L);
        user2.setUsername("Test2");

        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setMembers(new ArrayList<>());

        projectOutputDTO = new ProjectOutputDTO();
        projectOutputDTO.setId(1L);
        projectOutputDTO.setName("Test Project");

        updatedProjectOutputDTO = new ProjectOutputDTO();
        updatedProjectOutputDTO.setId(1L);
        updatedProjectOutputDTO.setName("Updated Name");

        ProjectMember projectMember = new ProjectMember();
        ProjectMemberKey projectMemberKey = new ProjectMemberKey();
        projectMemberKey.setProjectId(project.getId());
        projectMemberKey.setUserId(user.getId());
        projectMember.setRole(Role.ADMIN);
        projectMember.setId(projectMemberKey);
        projectMember.setProject(project);
        projectMember.setUser(user);

        project.getMembers().add(projectMember);
    }

    @Test
    public void testGetProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project result = projectService.getProject(1L);

        assertNotNull(result);
        assertEquals(project.getId(), result.getId());

        verify(projectRepository).findById(1L);
    }

    @Test
    public void testGetNonExistingProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            projectService.getProject(1L);
        });

        assertEquals("Project not found", exception.getMessage());

        verify(projectRepository).findById(1L);
    }

    @Test
    public void testGetProjectDTO() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(myUserService.getUser(any(Long.class))).thenReturn(user);
        when(projectMapper.toOutputDTO(any(Project.class))).thenReturn(projectOutputDTO);

        ProjectOutputDTO result = projectService.getProjectDTO(1L, 1L);

        assertNotNull(result);
        assertEquals(projectOutputDTO.getId(), result.getId());

        verify(projectRepository, times(2)).findById(1L);
    }

    @Test
    public void testCreateProject() {
        when(projectMapper.toEntity(any(ProjectInputDTO.class))).thenReturn(project);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.toOutputDTO(any(Project.class))).thenReturn(projectOutputDTO);
        when(projectRepository.findNamesByOwner(any(Long.class))).thenReturn(new ArrayList<>());

        ProjectOutputDTO result = projectService.createProject(projectInputDTO, 1L);

        assertNotNull(result);
        assertEquals("Test Project", result.getName());

        verify(projectMapper).toEntity(any(ProjectInputDTO.class));
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    public void testCreateProjectWithExistingName() {
        when(projectRepository.findNamesByOwner(any(Long.class))).thenReturn(List.of("Test Project"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(projectInputDTO, 1L);
        });

        assertEquals("Name already exists", exception.getMessage());
        verify(projectMapper, never()).toEntity(any(ProjectInputDTO.class));
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    public void testDeleteProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        assertDoesNotThrow(() -> {
            projectService.deleteProject(1L, 1L);
            verify(projectRepository).delete(any(Project.class));
        });
    }

    @Test
    public void testDeleteNonExistingProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            projectService.deleteProject(1L, 1L);
        });

        assertEquals("Project not found", exception.getMessage());

        verify(projectRepository).findById(1L);
    }

    @Test
    public void testEditProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        when(projectMapper.toOutputDTO(any(Project.class))).thenReturn(updatedProjectOutputDTO);

        ProjectOutputDTO result = projectService.editProject(1L, "Updated Name", 1L);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());

        verify(projectRepository, times(2)).findById(1L);
    }

    @Test
    public void testEditNonExistingProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            projectService.editProject(1L, "New Name", 1L);
        });

        assertEquals("Project not found", exception.getMessage());

        verify(projectRepository).findById(1L);
    }

    @Test
    public void testAddMember() {
        when(myUserService.getUser(any(Long.class))).thenReturn(user);
        when(projectRepository.findById(any(Long.class))).thenReturn(Optional.of(project));

        assertDoesNotThrow(() -> {
            projectService.addMember(1L, 2L, 1L);
            verify(projectMemberRepository).save(any(ProjectMember.class));
        });
    }

    @Test
    public void testKickProjectMember() {
        ProjectMemberKey memberKey2 = new ProjectMemberKey();
        memberKey2.setProjectId(1L);
        memberKey2.setUserId(2L);


        ProjectMember projectMember2 = new ProjectMember();
        projectMember2.setId(memberKey2);
        projectMember2.setUser(user2);
        projectMember2.setProject(project);

        project.getMembers().add(projectMember2);


        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        assertDoesNotThrow(() -> {
            projectService.kickProjectMember(1L, 2L, 1L);
            verify(projectMemberRepository).deleteByProjectIdAndUserId(1L, 2L);
        });
    }

    @Test
    public void testKickNonExistingProjectMember() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));


        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            projectService.kickProjectMember(1L, 2L, 1L);
        });

        assertEquals("Member not found", exception.getMessage());
    }

}

