package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.nanum.domain.Cart;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // 유틸리티 클래스이기 때문에 인스턴스화 방지
public class CartResponseDTO {
    @Builder
    @Getter
    @JsonPropertyOrder({"id","name","imgUrl","quantity","totalPrice"})
    public static class CartListItem {
        @Schema(example = "1",description = "장바구니 아이템 id")
        private Long id;
        @Schema(example = "1",description = "상품 id")
        private Long productId;
        @Schema(description = "상품 대표 이미지")
        private String imgUrl;
        @Schema(example = "토마토",description = "상품 이름")
        private String name;
        @Schema(example = "1000",description = "총 주문 가격")
        private Integer totalPrice;
        @Schema(example = "1",description = "상품 개수")
        private Integer quantity;

        public static CartListItem toDTO(Cart cart) {
            return CartListItem.builder()
                    .id(cart.getId())
                    .productId(cart.getProduct().getId())
                    .imgUrl(cart.getProduct().getImgUrl())
                    .name(cart.getProduct().getName())
                    .totalPrice((cart.getProduct().getPrice() * cart.getProductCount()))
                    .quantity(cart.getProductCount())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class CartList {
        @Schema(description = "장바구니 정보")
        List<CartListItem> items;

        public static CartList toDTO(List<CartListItem> cartListItems) {
            return CartList.builder().items(cartListItems).build();
        }
    }
}



