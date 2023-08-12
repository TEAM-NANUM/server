package server.nanum.dto.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record GuestLoginRequestDTO (
        @NotBlank(message = "초대코드는 비어있으면 안됩니다!")
        @JsonProperty("invite_code")
        String inviteCode) implements UserLoginRequestDTO{ }
