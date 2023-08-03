package server.nanum.dto.user.response;

import lombok.Builder;

@Builder
public record HostDTO(Long id, Long uid, String name) implements UserDTO {
}

