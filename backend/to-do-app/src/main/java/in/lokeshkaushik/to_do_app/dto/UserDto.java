package in.lokeshkaushik.to_do_app.dto;

import java.util.UUID;

// DTO for sending user details
public record UserDto(UUID id, String username, String emailId) {
}
