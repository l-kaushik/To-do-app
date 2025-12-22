package in.lokeshkaushik.to_do_app.dto.UserDto;

import jakarta.validation.constraints.*;

public record UserUpdateDto(
        @Size(min = 3, max = 13, message = "Username must be between 3 and 13 characters.")
        @Pattern(
                regexp = "^[a-zA-Z][a-zA-Z0-9_]*$",
                message = "Username must start with a letter and can contain only letters, numbers, and underscores."
        )
        String username,

        @Email(message = "Email must be valid")
        String emailId,

        @Size(min = 8, max = 128, message = "Password too short or too long.")
        String password
) {
}
