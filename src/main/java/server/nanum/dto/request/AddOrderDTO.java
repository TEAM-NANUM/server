package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;
import server.nanum.domain.User;
import server.nanum.domain.product.Product;

public record AddOrderDTO( //주문 등록 DTO
        @NotNull
        @JsonProperty("product_id")
        Long productId,
        @NotNull @Positive
        Integer quantity,
        @NotNull
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