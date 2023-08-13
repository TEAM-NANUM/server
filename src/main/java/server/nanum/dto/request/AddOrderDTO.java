package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.HQLSelect;
import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;
import server.nanum.domain.User;
import server.nanum.domain.product.Product;
@JsonPropertyOrder({"productId","quantity","address"})
public record AddOrderDTO( //주문 등록 DTO
        @NotBlank(message = "상품 번호를 입력해주세요")
        @Schema(example = "1",description = "상품 번호")
        @JsonProperty("product_id")
        @Positive(message = "상품 번호는 양수만 존재합니다")
        Long productId,
        @NotBlank(message = "상품개수를 입력해주세요")
        @Schema(example = "10",defaultValue = "1",description = "상품개수")
        @Positive(message = "상품 개수는 양수만 가능합니다")
        Integer quantity,
        @Valid
        @Schema(description = "배송 주소")
        @NotNull(message = "배송 주소를 입력해주세요")
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