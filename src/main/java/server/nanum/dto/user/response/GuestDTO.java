package server.nanum.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record GuestDTO(String nickname,
                                                @JsonProperty("zip_code") String zipCode,
                                                String defaultAddress,
                                                String detailAddress) implements UserDTO{

}
