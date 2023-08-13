package server.nanum.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonPropertyOrder({"toekn","userResponseDTO"})
public record LoginResponseDTO(
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",description = "토큰")
        @JsonProperty("token")
        String token,

        @JsonProperty("user")
        @Schema(description = "사용자 정보")
        UserResponseDTO userResponseDTO
) {
    @Builder
    @JsonPropertyOrder({"id","username","role"})
    public record UserResponseDTO(
            @Schema(example = "1",description = "사용자 번호")
            String id,

            @Schema(example = "나눔이",description = "사용자명")
            String username,

            @Schema(example = "ROLE_HOST",description = "사용자 권한")
            String role
    ) {
    }
}

