package antidimon.web.managervac.models.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Schema(description = "DTO для добавления участника в проект/задачу")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MemberInputDTO {

    @Schema(description = "Идентификатор пользователя", example = "1")
    @NotNull
    private long userId;

}
