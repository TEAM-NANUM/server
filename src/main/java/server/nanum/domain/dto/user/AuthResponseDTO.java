package server.nanum.domain.dto.user;

import lombok.Builder;


@Builder
public record AuthResponseDTO(String token, UserResponseDTO user) {

        @Builder
        public record UserResponseDTO(String id, String username, String role) {
        }
}
