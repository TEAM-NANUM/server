package server.nanum.dto.user.request;

import lombok.Builder;

@Builder
public record HostLoginRequestDTO(Long id, Long uid, String name) implements UserLoginRequestDTO {
}

