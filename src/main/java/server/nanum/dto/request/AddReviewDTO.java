package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import server.nanum.domain.Order;
import server.nanum.domain.Review;
@JsonPropertyOrder({"orderId","rating","comment"})
public record AddReviewDTO( //리뷰 등록 DTO
        @NotBlank(message = "주문 번호를 입력해주세요")
        @JsonProperty("order_id")
        @Positive(message = "주문 번호는 양수만 존재합니다")
        @Schema(example = "1",description = "주문 번호")
        Long orderId,
        @NotBlank(message = "상품의 별점을 입력해주세요")
        @Positive(message = "별점은 양수만 가능합니다")
        @Max(value=5,message = "별점의 최댓값은 5점 입니다")
        @Schema(example = "5.0",description = "상품 별점")
        Float rating,
        @NotNull(message = "상품의 후기가 존재하지 않습니다") //후기 작성 없이 별점만 줄 수도 있으니 notnull 사용
        @Schema(example = "음식이 맛있어요",description = "상품 후기")
        String comment) {
    public Review toEntity(Order order){ //DTO -> 리뷰 객체
        return Review.builder()
                .rating(rating)
                .comment(comment)
                .order(order)
                .build();
    }
}
