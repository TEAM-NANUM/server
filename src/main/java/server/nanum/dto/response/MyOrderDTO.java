package server.nanum.dto.response;

import server.nanum.domain.DeliveryStatus;

public record MyOrderDTO(
        Long orderId,
        String customer,
        Integer totalPrice,
        DeliveryStatus deliveryStatus,
        String name,
        Integer unit,
        Integer quantity){
}
