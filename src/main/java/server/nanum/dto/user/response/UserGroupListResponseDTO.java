package server.nanum.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserGroupListResponseDTO(
        HostDTO host,
        List<GuestDTO> members) {

    public record HostDTO(
            @Schema(example = "나눔이")
            String username,

            @Schema(example = "1000")
            int point) {
    }

    public record GuestDTO(
            @Schema(example = "나눔게스트")
            String nickname,

            @Schema(example = "서울시 강남구")
            @JsonProperty("default_address")
            String defaultAddress,

            @Schema(example = "abcd1234")
            @JsonProperty("invite_code")
            String inviteCode) {
    }
}


