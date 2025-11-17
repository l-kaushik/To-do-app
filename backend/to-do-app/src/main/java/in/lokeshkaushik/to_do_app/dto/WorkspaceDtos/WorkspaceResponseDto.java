package in.lokeshkaushik.to_do_app.dto.WorkspaceDtos;

import in.lokeshkaushik.to_do_app.dto.TaskDto.TaskResponseDto;

import java.util.List;
import java.util.UUID;

public record WorkspaceResponseDto(UUID uuid, String name, List<TaskResponseDto> tasks) {
}
