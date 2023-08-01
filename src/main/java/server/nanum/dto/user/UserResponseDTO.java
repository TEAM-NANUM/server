package server.nanum.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDTO {
    private final String userId;
    private final String name;
    private final boolean isGuest;
    private final int point;
}
