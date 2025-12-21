package in.lokeshkaushik.to_do_app.dto.TaskDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record TaskUpdateRequestDto(
        @NotNull(message = "Task UUID cannot be null")
        UUID uuid,

        @NotBlank(message = "Task rank cannot be empty")
        @Size(max = 100, message = "Task rank must not exceed 100 characters")
        @Pattern(
                regexp = "^[0-9a-z]+$",
                message = "Task rank can only contains lowercase letters and numbers"
        )
        String beforeRank,

        @NotBlank(message = "Task rank cannot be empty")
        @Size(max = 100, message = "Task rank must not exceed 100 characters")
        @Pattern(
                regexp = "^[0-9a-z]+$",
                message = "Task rank can only contains lowercase letters and numbers"
        )
        String afterRank,

        @Size(max = 500, message = "Description must not exceed max characters limit")
        String description,

        boolean completed
) implements TaskDto {
}
