package server.nanum.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import server.nanum.domain.User;

@Builder
@JsonPropertyOrder({"Id", "name", "point", "is_guest"})
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
                .isGuest(String.valueOf(user.getUserRole()).equals("GUEST"))
                .point(user.getUserGroupPoint())
                .build();
    }
}
