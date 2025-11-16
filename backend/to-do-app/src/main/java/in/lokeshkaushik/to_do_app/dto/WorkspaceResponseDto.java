package in.lokeshkaushik.to_do_app.dto;

import java.util.List;
import java.util.UUID;

public record WorkspaceResponseDto(UUID uuid, String name, List<TaskResponseDto> tasks) {
}
