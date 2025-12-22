package in.lokeshkaushik.to_do_app.dto.UserDto;

import java.util.UUID;

public record UserLoginResponseDto(UUID id, String username, String emailId) {
}
