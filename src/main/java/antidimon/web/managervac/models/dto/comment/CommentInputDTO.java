package antidimon.web.managervac.models.dto.comment;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(description = "Новое сообщение")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentInputDTO {

    @Schema(description = "Текст", example = "Hello")
    @NotBlank
    private String comment;
}
