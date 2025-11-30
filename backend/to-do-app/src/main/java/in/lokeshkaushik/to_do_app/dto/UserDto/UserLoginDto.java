package in.lokeshkaushik.to_do_app.dto.UserDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO for getting login details
public record UserLoginDto(
        @NotBlank(message = "Username/email cannot be empty")
        @Size(max = 50, message = "Username/email is too long")
        String identifier,
        @NotBlank(message = "Password cannot be empty")
        @Size(max = 128, message = "Password is too long")
        String password) {
}
