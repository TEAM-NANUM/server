package server.nanum.dto.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record GuestLoginRequestDTO (
        @NotBlank(message = "초대코드는 비어있으면 안됩니다!")
        @JsonProperty("invite_code")
        @Schema(description = "초대코드")
        String inviteCode) implements UserLoginRequestDTO{ }
