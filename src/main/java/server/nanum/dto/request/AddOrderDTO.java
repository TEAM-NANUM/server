package server.nanum.dto.request;

import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;
import server.nanum.domain.User;
import server.nanum.domain.product.Product;

public record AddOrderDTO(
        Long productId,
        Integer quantity,
        Integer zipCode,
        String defaultAddress,
        String detailAddress) {
    public Order toEntity(Product product, User user){
        return Order.builder()
                .productCount(quantity)
                .deliveryStatus(DeliveryStatus.PAYMENT_COMPLETE)
                .totalAmount(quantity*product.getPrice())
                .deliveryAddress(zipCode.toString()+defaultAddress+detailAddress)
                .product(product)
                .user(user)
                .build();
    }
}