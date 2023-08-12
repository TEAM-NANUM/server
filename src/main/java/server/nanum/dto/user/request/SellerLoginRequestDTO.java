package server.nanum.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record SellerLoginRequestDTO(
        @NotBlank(message = "이메일은 비어있으면 안됩니다!")
        String email,
        @NotBlank(message = "비밀번호는 비어있으면 안됩니다!")
        String password)
        implements UserLoginRequestDTO{
}
