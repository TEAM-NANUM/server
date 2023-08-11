package server.nanum.dto.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GuestLoginRequestDTO (@JsonProperty("invite_code")String inviteCode)
        implements UserLoginRequestDTO{
}
