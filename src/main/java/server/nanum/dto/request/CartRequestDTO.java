package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class CartRequestDTO {
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @JsonPropertyOrder({"productId","quantity"})
    public static class CartItem {
        @Schema(example = "1",description = "상품 번호")
        @Positive(message = "상품 번호는 양수만 존재합니다")
        @NotNull(message = "상품 번호를 입력해주세요")
        @JsonProperty("product_id")
        private Long productId;
        @Schema(defaultValue = "1",description = "상품 개수")
        @NotNull(message = "상품 개수를 입력해주세요")
        @Positive(message = "상품 개수는 양수만 가능합니다")
        private Integer quantity;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CartIdList {
        @Schema(description = "제거할 상품 번호")
        @JsonProperty("item_ids")
        @NotNull(message = "제거할 상품 번호를 입력해주세요")
        List<Long> itemIds;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @JsonPropertyOrder({"id","quantity"})
    public static class CartItemQuantity {
        @NotNull(message = "상품 번호를 입력해주세요")
        @Positive(message = "상품 번호는 양수만 존재합니다")
        @Schema(example = "1",description = "상품 번호")
        private Long id;
        @NotNull(message = "상품 개수를 입력해주세요")
        @Positive(message = "상품의 개수는 양수만 가능합니다")
        @Schema(defaultValue = "1",description = "상품 개수")
        private Integer quantity;
    }
}
