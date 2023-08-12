package server.nanum.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SellerLoginRequestDTO(
        @Schema(example = "user@example.com")
        @NotBlank(message = "이메일은 비어있으면 안됩니다!")
        String email,

        @Schema(example = "password123")
        @NotBlank(message = "비밀번호는 비어있으면 안됩니다!")
        String password) implements UserLoginRequestDTO{
}
