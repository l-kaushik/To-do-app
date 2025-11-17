package in.lokeshkaushik.to_do_app.dto.WorkspaceDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record WorkspaceUpdateRequestDto(
        @NotNull(message = "Workspace UUID cannot be null")
        UUID uuid,

        @NotBlank(message = "Workspace name cannot be empty")
        @Size(max = 100, message = "Workspace name must not exceed 100 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Workspace name can only contain letters, numbers, and underscores.")
        String name){
}
