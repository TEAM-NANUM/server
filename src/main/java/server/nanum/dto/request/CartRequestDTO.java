package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CartRequestDTO {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CartItem {
        @JsonProperty("product_id")
        private Long productId;

        @Positive
        private Integer quantity;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CartIdList {
        @JsonProperty("item_ids")
        List<Long> itemIds;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CartItemQuantity {
        private Long id;

        @Positive
        private Integer quantity;
    }
}
