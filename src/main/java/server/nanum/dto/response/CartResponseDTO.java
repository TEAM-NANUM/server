package server.nanum.dto.response;

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
    public static class CartListItem {
        private Long id;
        private String imgUrl;
        private String name;
        private Integer totalPrice;
        private Integer quantity;

        public static CartListItem toDTO(Cart cart) {
            return CartListItem.builder()
                    .id(cart.getId())
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
        List<CartListItem> items;

        public static CartList toDTO(List<CartListItem> cartListItems) {
            return CartList.builder().items(cartListItems).build();
        }
    }
}



