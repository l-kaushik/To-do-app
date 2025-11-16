package in.lokeshkaushik.to_do_app.dto;

import in.lokeshkaushik.to_do_app.model.Task;

import java.util.List;
import java.util.UUID;

public record WorkspaceResponseDto(UUID id, String name, List<Task> tasks) {
}
