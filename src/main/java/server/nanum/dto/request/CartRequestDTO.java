package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

public class CartRequestDTO {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CartItem {
        @JsonProperty("product_id")
        private Long productId;

        private Integer quantity;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CartIdList {
        @JsonProperty("item_ids")
        List<Long> itemIds;
    }
}
