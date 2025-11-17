package in.lokeshkaushik.to_do_app.dto.WorkspaceDtos;

import in.lokeshkaushik.to_do_app.dto.TaskDto.TaskCreateRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record WorkspaceCreateRequestDto(
        @NotBlank(message = "Workspace name cannot be empty")
        @Size(max = 100, message = "Workspace name must not exceed 100 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores.")
        String name,
        List<TaskCreateRequestDto> tasks) {
}
