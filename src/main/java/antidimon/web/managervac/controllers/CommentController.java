package antidimon.web.managervac.controllers;


import antidimon.web.managervac.models.dto.comment.CommentInputDTO;
import antidimon.web.managervac.models.dto.comment.CommentOutputDTO;
import antidimon.web.managervac.security.jwt.JwtTokenUtil;
import antidimon.web.managervac.services.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks/{taskId}/comments")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping
    public ResponseEntity<List<CommentOutputDTO>> getTaskComments(@RequestHeader("Authorization") String jwt,
                                                                  @PathVariable("projectId") long projectId,
                                                                  @PathVariable("taskId") long taskId ) {
        long senderId = jwtTokenUtil.getId(jwt);
        var comments = commentService.getTaskCommentsDTO(projectId, taskId, senderId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentOutputDTO> getComment(@RequestHeader("Authorization") String jwt,
                                                       @PathVariable("projectId") long projectID,
                                                       @PathVariable("id") long id){
        long senderId = jwtTokenUtil.getId(jwt);
        var comment = commentService.getCommentDTO(projectID, id, senderId);
        return ResponseEntity.ok(comment);
    }

    @PostMapping
    public ResponseEntity<CommentOutputDTO> createComment(@RequestHeader("Authorization") String jwt,
                                                          @RequestBody @Valid CommentInputDTO commentInputDTO,
                                                          @PathVariable("projectId") long projectID,
                                                          @PathVariable("taskId") long taskId){
        long senderId = jwtTokenUtil.getId(jwt);
        var comment = commentService.createComment(projectID, taskId, commentInputDTO, senderId);
        return ResponseEntity.ok(comment);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommentOutputDTO> editComment(@RequestHeader("Authorization") String jwt,
                                                        @RequestBody @Valid CommentInputDTO commentInputDTO,
                                                        @PathVariable("projectId") long projectId,
                                                        @PathVariable("taskId") long taskId,
                                                        @PathVariable("id") long id){
        long senderId = jwtTokenUtil.getId(jwt);
        var comment = commentService.editComment(projectId, taskId, id, commentInputDTO.getComment(), senderId);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@RequestHeader("Authorization") String jwt,
                                                        @PathVariable("projectId") long projectId,
                                                        @PathVariable("taskId") long taskId,
                                                        @PathVariable("id") long id){
        long senderId = jwtTokenUtil.getId(jwt);
        commentService.deleteComment(projectId, taskId, id, senderId);
        return ResponseEntity.ok("Deleted successfully");
    }


}
