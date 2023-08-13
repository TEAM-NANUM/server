package server.nanum.dto.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import server.nanum.domain.Address;
import server.nanum.domain.Delivery;

import java.util.List;

public record DeliveryListResponse(
        @JsonProperty("delivery_address")
        @Schema(description = "배송지 정보")
        List<DeliveryResponse> deliveryResponses) {
    @JsonPropertyOrder({"deliveryId","nickname","isDefault","address","phoneNumber","username"})
    public record DeliveryResponse(
            @Schema(example = "1",description = "배송지 번호")
            @JsonProperty("delivery_id")
            Long deliveryId,

            @Schema(example = "집",description = "주소지 별칭")
            String nickname,

            @Schema(example = "true",description = "기본 배송지 여부")
            @JsonProperty("is_default")
            Boolean isDefault,

            @Schema(example = "010-1234-5678",description = "배송지 전화번호")
            @JsonProperty("phone_number")
            String phoneNumber,

            @Schema(example = "나눔이",description = "수신자명")
            @JsonProperty("receiver")
            String username,
            @Schema(description = "배송지 주소")
            Address address
    ) {
        // 이 부분에 필요한 추가적인 메서드나 로직을 넣을 수 있습니다.

        public static DeliveryResponse fromEntity(Delivery delivery) {
            return new DeliveryResponse(
                    delivery.getId(),
                    delivery.getNickname(),
                    delivery.isDefault(),
                    delivery.getPhoneNumber(),
                    delivery.getReceiver(),
                    delivery.getAddress()
            );
        }
    }
}