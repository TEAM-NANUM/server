package server.nanum.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import server.nanum.domain.User;

@Builder
@JsonPropertyOrder({"Id", "name", "point", "is_guest"})
public record HostGetResponseDTO(
        @Schema(example = "1",description = "사용자 번호")
        String Id,

        @Schema(example = "나눔이",description = "사용자명")
        String name,

        @Schema(example = "true",description = "게스트 여부")
        @JsonProperty("is_guest") boolean isGuest,

        @Schema(example = "1000",description = "포인트")
        int point
) {
    public static HostGetResponseDTO toDTO(User user) {
        return HostGetResponseDTO.builder()
                .Id(String.valueOf(user.getId()))
                .name(user.getName())
                .isGuest(String.valueOf(user.getUserRole()).equals("GUEST"))
                .point(user.getUserGroupPoint())
                .build();
    }
}
