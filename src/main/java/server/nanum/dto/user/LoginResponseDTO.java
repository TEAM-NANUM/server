package server.nanum.dto.user;

import lombok.Builder;


@Builder
public record LoginResponseDTO(String token, UserResponseDTO user) {

        @Builder
        public record UserResponseDTO(String id, String username, String role) {
        }
}
