package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
@JsonPropertyOrder({"userName","point"})
public record SellerInfoDTO( //판매자 정보 DTO
        @JsonProperty("user_name")
        @Schema(example = "나눔이",description = "판매자명")
        String userName,
        @Schema(example = "1000",description = "판매자 포인트")
        Long point) {
}
