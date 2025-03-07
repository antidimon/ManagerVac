package antidimon.web.managervac.models.dto.comment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentInputDTO {

    @NotNull
    private long taskId;
    @NotNull
    private long userId;
    @NotBlank
    private String comment;
}
