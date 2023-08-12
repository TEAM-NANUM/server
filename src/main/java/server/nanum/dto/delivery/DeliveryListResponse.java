package server.nanum.dto.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import server.nanum.domain.Address;
import server.nanum.domain.Delivery;

import java.util.List;

public record DeliveryListResponse(@JsonProperty("delivery_address") List<DeliveryResponse> deliveryResponses) {
    public record DeliveryResponse(
            @Schema(example = "1")
            @JsonProperty("delivery_id")
            Long deliveryId,

            @Schema(example = "집")
            String nickname,

            @Schema(example = "true")
            @JsonProperty("is_default")
            Boolean isDefault,

            @Schema(example = "010-1234-5678")
            @JsonProperty("phone_number")
            String phoneNumber,

            @Schema(example = "나눔이")
            @JsonProperty("receiver")
            String username,

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