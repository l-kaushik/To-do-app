package in.lokeshkaushik.to_do_app.dto;

import java.util.UUID;

public record TaskResponseDto(UUID uuid, String name, String description, boolean completed) implements TaskDto{
}
