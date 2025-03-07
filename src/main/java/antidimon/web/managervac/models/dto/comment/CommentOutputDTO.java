package antidimon.web.managervac.models.dto.comment;


import antidimon.web.managervac.models.dto.user.MyUserIdNameOutputDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CommentOutputDTO {

    private long id;
    private MyUserIdNameOutputDTO user;
    private String comment;
}
