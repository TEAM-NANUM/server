package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.nanum.domain.DeliveryStatus;

public record MyOrderDTO( //주문조회 단건 정보
        @JsonProperty("order_id")
        Long orderId,
        String customer,
        @JsonProperty("total_price")
        Integer totalPrice,
        @JsonProperty("delivery_status")
        DeliveryStatus deliveryStatus,
        String name,
        Integer unit,
        Integer quantity){
}
