package in.lokeshkaushik.to_do_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UserLoginResponseDto(UUID id, String username, String emailId, String jwtToken) {
}
