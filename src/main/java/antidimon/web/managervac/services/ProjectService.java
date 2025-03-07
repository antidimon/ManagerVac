package antidimon.web.managervac.services;


import antidimon.web.managervac.mappers.ProjectMapper;
import antidimon.web.managervac.mappers.TaskMapper;
import antidimon.web.managervac.models.dto.project.ProjectInputDTO;
import antidimon.web.managervac.models.dto.project.ProjectOutputDTO;
import antidimon.web.managervac.models.dto.task.TaskOutputDTO;
import antidimon.web.managervac.models.entities.MyUser;
import antidimon.web.managervac.models.entities.Project;
import antidimon.web.managervac.models.entities.ProjectMember;
import antidimon.web.managervac.models.entities.ProjectMemberKey;
import antidimon.web.managervac.models.enums.Role;
import antidimon.web.managervac.repositories.ProjectMemberRepository;
import antidimon.web.managervac.repositories.ProjectRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectService {

    private final MyUserService myUserService;
    private ProjectRepository projectRepository;
    private ProjectMemberRepository projectMemberRepository;
    private ProjectMapper projectMapper;
    private TaskMapper taskMapper;

    public Project getProject(long projectId) throws NoSuchElementException {
        Optional<Project> project = this.projectRepository.findById(projectId);
        if (project.isEmpty()) throw new NoSuchElementException("Project not found");
        return project.get();
    }

    public ProjectOutputDTO getProjectDTO(long projectId, long senderId) throws NoSuchElementException {
        this.checkUserMemberOfProject(projectId, senderId);
        Project project = this.getProject(projectId);
        return projectMapper.toOutputDTO(project);
    }

    private List<Project> getUserProjects(long userId) {
        return this.projectRepository.findAllUserProjects(userId);
    }

    public List<ProjectOutputDTO> getUserProjectsDTO(long userId) {
        var list = this.getUserProjects(userId);
        return list.stream().map(projectMapper::toOutputDTO).toList();
    }

    @Transactional
    public ProjectOutputDTO createProject(@Valid ProjectInputDTO projectInputDTO, long userId) throws IllegalArgumentException{

        if (this.isNameValid(projectInputDTO.getName(), userId)){
            Project project = projectMapper.toEntity(projectInputDTO);
            projectRepository.save(project);
            ProjectMember projectMember = this.createMember(project, userId, Role.ADMIN);
            project.setMembers(List.of(projectMember));
            return projectMapper.toOutputDTO(project);
        }else {
            throw new IllegalArgumentException("Name already exists");
        }
    }

    @Transactional
    public void deleteProject(long senderId, long projectId) throws NoSuchElementException, SecurityException{
        this.checkUserOwnProject(projectId, senderId);
        Project project = this.getProject(projectId);
        projectRepository.delete(project);
    }

    @Transactional
    public void editProject(long projectId, String newName, long senderId) throws NoSuchElementException, SecurityException{
        this.checkUserOwnProject(projectId, senderId);
        Project project = this.getProject(projectId);
        project.setName(newName);
        projectRepository.save(project);

    }

    @Transactional
    public void addMember(long projectId, long newUserId, long senderId) throws NoSuchElementException, SecurityException{
        this.checkUserOwnProject(projectId, senderId);
        Project project = this.getProject(projectId);
        ProjectMember projectMember = this.createMember(project, newUserId, Role.MEMBER);
        projectMemberRepository.save(projectMember);
    }

    private ProjectMember createMember(Project project, long userId, Role role){
        MyUser user = myUserService.getUser(userId);
        ProjectMember projectMember = new ProjectMember();

        ProjectMemberKey projectMemberKey = new ProjectMemberKey();
        projectMemberKey.setProjectId(project.getId());
        projectMemberKey.setUserId(userId);

        projectMember.setId(projectMemberKey);
        projectMember.setUser(user);
        projectMember.setProject(project);
        projectMember.setRole(role);

        return projectMember;
    }

    protected void checkUserOwnProject(long projectId, long userId) throws SecurityException{
        Project project = this.getProject(projectId);
        long ownerId = project.getMembers().stream()
                .filter(member -> member.getRole().equals(Role.ADMIN)).findFirst().get().getUser().getId();
        if (ownerId != userId) throw new SecurityException("Permission denied");
    }

    protected void checkUserMemberOfProject(long projectId, long userId) throws SecurityException{
        Project project = this.getProject(projectId);
        boolean flag = project.getMembers().stream()
                .anyMatch(member -> member.getId().getUserId() == userId);
        if (!flag) throw new SecurityException("Permission denied");

    }

    private boolean isNameValid(@NotBlank String name, @NotNull long ownerId) {
        List<String> names = this.projectRepository.findNamesByOwner(ownerId);
        return !names.contains(name);
    }

    @Transactional
    public void kickProjectMember(long projectId, long kickUserId, long senderId) throws NoSuchElementException, SecurityException {
        this.checkUserOwnProject(projectId, senderId);
        Project project = this.getProject(projectId);
        Optional<ProjectMember> projectMember = project.getMembers().stream()
                .filter(member -> member.getId().getUserId() == kickUserId).findFirst();
        if (projectMember.isPresent()){
            projectMemberRepository.deleteByProjectIdAndUserId(projectId, kickUserId);
        }else{
            throw new NoSuchElementException("Member not found");
        }
    }

}
