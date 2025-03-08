package antidimon.web.managervac.services;

import antidimon.web.managervac.mappers.CommentMapper;
import antidimon.web.managervac.models.dto.comment.CommentInputDTO;
import antidimon.web.managervac.models.dto.comment.CommentOutputDTO;
import antidimon.web.managervac.models.entities.Comment;
import antidimon.web.managervac.models.entities.MyUser;
import antidimon.web.managervac.models.entities.Project;
import antidimon.web.managervac.models.entities.Task;
import antidimon.web.managervac.repositories.CommentRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final MyUserService myUserService;
    private CommentRepository commentRepository;
    private TaskService taskService;
    private ProjectService projectService;

    public List<Comment> getTaskComments(long taskId) {
        Task task = taskService.getProjectTask(taskId);
        return task.getComments();
    }

    public List<CommentOutputDTO> getTaskCommentsDTO(long projectId, long taskId, long senderId) {
        projectService.checkUserMemberOfProject(projectId, senderId);
        var comments = this.getTaskComments(taskId);
        return comments.stream().map(commentMapper::toOutputDTO).toList();
    }

    public Comment getComment(long commentId)
            throws NoSuchElementException {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) return comment.get();
        throw new NoSuchElementException("Comment not found");
    }

    public CommentOutputDTO getCommentDTO(long projectId, long commentId, long senderId)
            throws NoSuchElementException, SecurityException {
        projectService.checkUserMemberOfProject(projectId, senderId);
        var comment = this.getComment(commentId);
        return commentMapper.toOutputDTO(comment);
    }

    @Transactional
    public CommentOutputDTO createComment(long projectId, long taskId, CommentInputDTO commentInputDTO, long senderId)
            throws NoSuchElementException, SecurityException {
        this.checkUserRightsToComment(projectId, taskId, senderId);
        Task task = taskService.getProjectTask(projectId);
        MyUser user = myUserService.getUser(senderId);
        Comment comment = commentMapper.toEntity(commentInputDTO);
        comment.setUser(user);
        comment.setTask(task);
        commentRepository.save(comment);
        return commentMapper.toOutputDTO(comment);
    }

    @Transactional
    public CommentOutputDTO editComment(long projectId, long taskId, long id, String text, long senderId)
            throws NoSuchElementException, SecurityException {
        this.checkUserRightsToComment(projectId, taskId, senderId);
        Comment comment = this.getComment(id);
        if (comment.getUser().getId() != senderId) throw new SecurityException("You do not have permission to edit this comment");
        comment.setComment(text);
        commentRepository.save(comment);
        return commentMapper.toOutputDTO(comment);
    }

    @Transactional
    public void deleteComment(long projectId, long taskId, long id, long senderId)
            throws NoSuchElementException, SecurityException {
        this.checkUserRightsToComment(projectId, taskId, senderId);
        Comment comment = this.getComment(id);
        Project project = projectService.getProject(projectId);
        if (comment.getUser().getId() != senderId && projectService.getProjectOwnerId(project) != senderId )
            throw new SecurityException("You do not have permission to delete this comment");
        commentRepository.delete(comment);
    }

    private void checkUserRightsToComment(long projectId, long taskId ,long userId)
            throws SecurityException {
        projectService.checkUserMemberOfProject(projectId, userId);
        taskService.checkUserDevelopTask(taskId, userId);
    }
}
