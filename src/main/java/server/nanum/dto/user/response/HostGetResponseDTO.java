package server.nanum.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import server.nanum.domain.User;

@Builder
public record HostGetResponseDTO(
        String Id,
        String name,
        @JsonProperty("is_guest") boolean isGuest,
        int point)
{
    public static HostGetResponseDTO toDTO(User user) {
        return HostGetResponseDTO.builder()
                .Id(String.valueOf(user.getId()))
                .name(user.getName())
                .isGuest(false)
                .point(user.getUserGroupPoint())
                .build();
    }
}
