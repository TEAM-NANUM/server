package server.nanum.dto.request;

import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;
import server.nanum.domain.User;
import server.nanum.domain.product.Product;

public record AddOrderDTO( //주문 등록 DTO
        Long productId,
        Integer quantity,
        AddressDTO address) {
    public Order toEntity(Product product, User user){ //DTO -> 주문 객체로 리턴
        return Order.builder()
                .productCount(quantity)
                .deliveryStatus(DeliveryStatus.PAYMENT_COMPLETE)
                .totalAmount(quantity*product.getPrice())
                .deliveryAddress(address.toString())
                .product(product)
                .user(user)
                .build();
    }
}