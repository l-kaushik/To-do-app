package in.lokeshkaushik.to_do_app.dto.WorkspaceDtos;

import java.util.UUID;

public record WorkspaceFullResponseDto(UUID uuid, String name, int tasksCount) {
}
