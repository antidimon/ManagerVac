package antidimon.web.managervac.models.dto.projectMember;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectMemberInputDTO {

    @NotNull
    private long userId;

}
