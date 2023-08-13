package server.nanum.dto.user.request;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
@JsonPropertyOrder({"email","password"})
public record SellerLoginRequestDTO(
        @Schema(example = "user@example.com",description = "판매자 이메일")
        @NotBlank(message = "이메일은 비어있으면 안됩니다!")
        String email,

        @Schema(example = "password123",description = "판매자 계정 비밀번호")
        @NotBlank(message = "비밀번호는 비어있으면 안됩니다!")
        String password) implements UserLoginRequestDTO{
}
