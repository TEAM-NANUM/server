package server.nanum.dto.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public record GuestLoginRequestDTO (@JsonProperty("invite_code")String inviteCode)
        implements UserLoginRequestDTO{
}
