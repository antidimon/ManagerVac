package antidimon.web.managervac.models.dto.comment;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentInputDTO {

    @NotBlank
    private String comment;
}
