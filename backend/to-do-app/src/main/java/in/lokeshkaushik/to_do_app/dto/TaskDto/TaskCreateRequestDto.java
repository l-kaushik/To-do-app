package in.lokeshkaushik.to_do_app.dto.TaskDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TaskCreateRequestDto(
        @Size(max = 500, message = "Description must not exceed max characters limit")
        String description,
        boolean completed
) implements TaskDto{
}
