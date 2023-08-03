package server.nanum.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
// 로그인 응답 데이터 전송 객체
public record LoginResponseDTO(
        @JsonProperty("token") String token,
        @JsonProperty("user") UserResponseDTO userResponseDTO
) {
    @Builder
    public record UserResponseDTO(String id, String username, String role) {
    }
}

