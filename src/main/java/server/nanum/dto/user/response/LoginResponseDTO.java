package server.nanum.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record LoginResponseDTO(
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
        @JsonProperty("token")
        String token,

        @JsonProperty("user")
        UserResponseDTO userResponseDTO
) {
    @Builder
    public record UserResponseDTO(
            @Schema(example = "1")
            String id,

            @Schema(example = "나눔이")
            String username,

            @Schema(example = "ROLE_HOST")
            String role
    ) {
    }
}

