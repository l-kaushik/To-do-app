package in.lokeshkaushik.to_do_app.dto;

import java.util.UUID;

public record WorkspaceCreateResponseDto(UUID uuid, String name, int tasksCreated) {
}
