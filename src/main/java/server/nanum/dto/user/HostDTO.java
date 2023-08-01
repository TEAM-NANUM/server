package server.nanum.dto.user;

import lombok.Builder;

@Builder
public record HostDTO(Long id, Long uid, String name) implements UserDTO {
}

