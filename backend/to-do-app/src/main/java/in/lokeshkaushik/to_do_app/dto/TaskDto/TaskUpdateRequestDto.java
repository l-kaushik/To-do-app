package in.lokeshkaushik.to_do_app.dto.TaskDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record TaskUpdateRequestDto(
        @NotNull(message = "Task UUID cannot be null")
        UUID uuid,

        @NotBlank(message = "Task name cannot be empty")
        @Size(max = 100, message = "Task name must not exceed 100 characters")
        @Pattern(
                regexp = "^[a-zA-Z][a-zA-Z0-9_]*$",
                message = "Task name must start with a letter and can only contain letters, numbers, and underscores."
        )
        String name,

        @Size(max = 500, message = "Description must not exceed max characters limit")
        String description,

        boolean completed
) implements TaskDto {
}
