package antidimon.web.managervac.models.dto.comment;


import antidimon.web.managervac.models.dto.user.MyUserIdNameOutputDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createdAt;
}
