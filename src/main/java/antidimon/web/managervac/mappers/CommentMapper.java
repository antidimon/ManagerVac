package antidimon.web.managervac.mappers;

import antidimon.web.managervac.models.dto.comment.CommentInputDTO;
import antidimon.web.managervac.models.dto.comment.CommentOutputDTO;
import antidimon.web.managervac.models.entities.Comment;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommentMapper {

    private final ModelMapper modelMapper = new ModelMapper();
    private MyUserMapper myUserMapper;

    public Comment toEntity(CommentInputDTO commentInputDTO){
        return modelMapper.map(commentInputDTO, Comment.class);
    }

    public CommentOutputDTO toOutputDTO(Comment comment) {
        return CommentOutputDTO.builder()
                .id(comment.getId())
                .user(myUserMapper.toIdNameOutputDTO(comment.getUser()))
                .comment(comment.getComment())
                .build();
    }
}
