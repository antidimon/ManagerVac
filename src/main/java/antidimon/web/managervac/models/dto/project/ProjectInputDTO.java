package antidimon.web.managervac.models.dto.project;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectInputDTO {

    @NotBlank
    private String name;
    @NotNull
    private long ownerId;
}
