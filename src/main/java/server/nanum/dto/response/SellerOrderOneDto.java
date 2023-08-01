package server.nanum.dto.response;

import server.nanum.domain.DeliveryStatus;

public record SellerOrderOneDto(
        Long id,
        Integer quantity,
        String userName,
        DeliveryStatus deliveryStatus) {
}
