package server.nanum.dto.response;

import server.nanum.domain.DeliveryStatus;

public record MyOrderDTO( //주문조회 단건 정보
        Long orderId,
        String customer,
        Integer totalPrice,
        DeliveryStatus deliveryStatus,
        String name,
        Integer unit,
        Integer quantity){
}
