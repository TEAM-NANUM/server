package server.nanum.dto.user.response;

import lombok.Builder;

@Builder
public record GuestDTO(String nickname, String zipCode, String defaultAddress, String detailAddress) {
}
