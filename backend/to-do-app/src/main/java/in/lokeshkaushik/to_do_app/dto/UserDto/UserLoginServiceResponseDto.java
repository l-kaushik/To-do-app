package in.lokeshkaushik.to_do_app.dto.UserDto;

public record UserLoginServiceResponseDto(UserLoginResponseDto loginResponseDto, String jwtToken) {
}
