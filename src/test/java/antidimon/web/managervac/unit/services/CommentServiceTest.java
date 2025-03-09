package antidimon.web.managervac.unit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import antidimon.web.managervac.mappers.CommentMapper;
import antidimon.web.managervac.models.dto.comment.CommentInputDTO;
import antidimon.web.managervac.models.dto.comment.CommentOutputDTO;
import antidimon.web.managervac.models.entities.Comment;
import antidimon.web.managervac.models.entities.MyUser;
import antidimon.web.managervac.models.entities.Project;
import antidimon.web.managervac.models.entities.Task;
import antidimon.web.managervac.repositories.CommentRepository;
import antidimon.web.managervac.services.CommentService;
import antidimon.web.managervac.services.MyUserService;
import antidimon.web.managervac.services.ProjectService;
import antidimon.web.managervac.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private ProjectService projectService;

    @Mock
    private MyUserService myUserService;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    private CommentInputDTO commentInputDTO;
    private Comment comment;
    private Task task;
    private Project project;
    private MyUser user;
    private CommentOutputDTO commentOutputDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        commentInputDTO = new CommentInputDTO();
        commentInputDTO.setComment("Test Comment");

        user = new MyUser();
        user.setId(1L);

        project = new Project();
        project.setId(1L);

        task = new Task();
        task.setId(1L);
        task.setComments(new ArrayList<>());

        comment = new Comment();
        comment.setId(1L);
        comment.setComment("Test Comment");
        comment.setUser(user);
        comment.setTask(task);

        task.getComments().add(comment);

        commentOutputDTO = new CommentOutputDTO();
        commentOutputDTO.setId(1L);
        commentOutputDTO.setComment("Test Comment");
    }

    @Test
    public void testGetTaskComments() {
        when(taskService.getProjectTask(1L)).thenReturn(task);

        List<Comment> result = commentService.getTaskComments(1L);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(taskService).getProjectTask(1L);
    }

    @Test
    public void testGetTaskCommentsDTO() {
        doNothing().when(projectService).checkUserMemberOfProject(any(Long.class), any(Long.class));
        when(taskService.getProjectTask(1L)).thenReturn(task);
        when(commentMapper.toOutputDTO(any(Comment.class))).thenReturn(commentOutputDTO);

        List<CommentOutputDTO> result = commentService.getTaskCommentsDTO(1L, 1L, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(commentMapper).toOutputDTO(any(Comment.class));
    }

    @Test
    public void testGetComment() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Comment result = commentService.getComment(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(commentRepository).findById(1L);
    }

    @Test
    public void testGetNonExistingComment() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            commentService.getComment(1L);
        });

        assertEquals("Comment not found", exception.getMessage());

        verify(commentRepository).findById(1L);
    }

    @Test
    public void testGetCommentDTO() {
        doNothing().when(projectService).checkUserMemberOfProject(any(Long.class), any(Long.class));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentMapper.toOutputDTO(any(Comment.class))).thenReturn(commentOutputDTO);

        CommentOutputDTO result = commentService.getCommentDTO(1L, 1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(commentMapper).toOutputDTO(any(Comment.class));
    }

    @Test
    public void testCreateComment() {
        doNothing().when(projectService).checkUserMemberOfProject(any(Long.class), any(Long.class));
        doNothing().when(taskService).checkUserDevelopTask(any(Long.class), any(Long.class));
        when(taskService.getProjectTask(any(Long.class))).thenReturn(task);
        when(myUserService.getUser(any(Long.class))).thenReturn(user);
        when(commentMapper.toEntity(any(CommentInputDTO.class))).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toOutputDTO(any(Comment.class))).thenReturn(commentOutputDTO);

        CommentOutputDTO result = commentService.createComment(1L, 1L, commentInputDTO, 1L);

        assertNotNull(result);
        assertEquals("Test Comment", result.getComment());

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    public void testEditComment() {
        doNothing().when(projectService).checkUserMemberOfProject(any(Long.class), any(Long.class));
        doNothing().when(taskService).checkUserDevelopTask(any(Long.class), any(Long.class));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentMapper.toOutputDTO(any(Comment.class))).thenReturn(commentOutputDTO);

        CommentOutputDTO result = commentService.editComment(1L, 1L, 1L, "Updated Comment", 1L);

        assertNotNull(result);
        assertEquals("Test Comment", result.getComment());

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    public void testEditCommentWithWrongUser() {
        doNothing().when(projectService).checkUserMemberOfProject(any(Long.class), any(Long.class));
        doNothing().when(taskService).checkUserDevelopTask(any(Long.class), any(Long.class));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        user.setId(2L);

        Exception exception = assertThrows(SecurityException.class, () -> {
            commentService.editComment(1L, 1L, 1L, "Updated Comment", 1L);
        });

        assertEquals("You do not have permission to edit this comment", exception.getMessage());

        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    public void testDeleteComment() {
        doNothing().when(projectService).checkUserMemberOfProject(any(Long.class), any(Long.class));
        doNothing().when(taskService).checkUserDevelopTask(any(Long.class), any(Long.class));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(projectService.getProject(any(Long.class))).thenReturn(project);
        when(projectService.getProjectOwnerId(any(Project.class))).thenReturn(user.getId());

        assertDoesNotThrow(() -> {
            commentService.deleteComment(1L, 1L, 1L, 1L);
            verify(commentRepository).delete(any(Comment.class));
        });
    }

    @Test
    public void testDeleteCommentWithWrongUser() {
        doNothing().when(projectService).checkUserMemberOfProject(any(Long.class), any(Long.class));
        doNothing().when(taskService).checkUserDevelopTask(any(Long.class), any(Long.class));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(projectService.getProject(any(Long.class))).thenReturn(project);
        user.setId(2L);
        when(projectService.getProjectOwnerId(any(Project.class))).thenReturn(2L);

        Exception exception = assertThrows(SecurityException.class, () -> {
            commentService.deleteComment(1L, 1L, 1L, 1L);
        });

        assertEquals("You do not have permission to delete this comment", exception.getMessage());

        verify(commentRepository, never()).delete(any(Comment.class));
    }
}

