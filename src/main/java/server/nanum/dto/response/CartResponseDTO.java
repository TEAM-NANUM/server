package server.nanum.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // 유틸리티 클래스이기 때문에 인스턴스화 방지
public class CartResponseDTO {
    @Builder
    @Getter
    public static class CartListItem {
        private Long id;
        private String imgUrl;
        private Integer totalPrice;
        private Integer quantity;
    }

    @Builder
    @Getter
    public static class CartList {
        List<CartListItem> items;
    }
}



