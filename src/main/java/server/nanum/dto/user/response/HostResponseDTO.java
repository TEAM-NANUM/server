package server.nanum.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import server.nanum.domain.User;

@Builder
public record HostResponseDTO (
        String Id,
        String name,
        @JsonProperty("is_guest") boolean isGuest,
        int point)
{
    public static HostResponseDTO toDTO(User user) {
        return HostResponseDTO.builder()
                .Id(String.valueOf(user.getId()))
                .name(user.getName())
                .isGuest(false)
                .point(user.getUserGroupPoint())
                .build();
    }
}
