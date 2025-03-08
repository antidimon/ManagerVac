package antidimon.web.managervac.models.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "DTO для смены имени проекта")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectEditDTO {

    @Schema(description = "Новое имя проекта", example = "Test")
    @NotBlank
    private String name;
}
