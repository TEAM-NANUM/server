package server.nanum.dto.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.nanum.domain.Address;
import server.nanum.domain.Delivery;

import java.util.List;

public record DeliveryListResponse(@JsonProperty("delivery_address") List<DeliveryResponse> deliveryResponses) {

    // DeliveryResponse 레코드 정의
    public record DeliveryResponse(
            @JsonProperty("delivery_id")
            Long deliveryId,
            String nickname,
            @JsonProperty("is_default")
            Boolean isDefault,
            @JsonProperty("phone_number")
            String phoneNumber,
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
                    delivery.getUsername(),
                    delivery.getAddress()
            );
        }
    }
}