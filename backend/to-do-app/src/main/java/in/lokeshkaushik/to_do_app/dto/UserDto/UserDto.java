package in.lokeshkaushik.to_do_app.dto.UserDto;

import java.util.UUID;

// DTO for sending user details
public record UserDto(UUID uuid, String username, String emailId) {
}
