package antidimon.web.managervac.models.dto.project;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(description = "DTO для создания проекта")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectInputDTO {

    @Schema(description = "Имя проекта", example = "Test")
    @NotBlank
    private String name;
}
