package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UserGroupListResponseDTO(HostDTO host, List<GuestDTO> members) {
    public  record HostDTO(String username, int point) {
    }

    public  record GuestDTO(String nickname,
                            @JsonProperty("default_address") String defaultAddress,
                            @JsonProperty("invite_code") String inviteCode) {
    }
}

