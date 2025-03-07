package antidimon.web.managervac.models.dto.user;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MemberInputDTO {

    @NotNull
    private long userId;

}
