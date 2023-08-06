package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SellerInfoDTO( //판매자 정보 DTO
        @JsonProperty("user_name")
        String userName,
        Long point) {
}
