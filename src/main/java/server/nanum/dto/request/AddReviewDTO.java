package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import server.nanum.domain.Order;
import server.nanum.domain.Review;
public record AddReviewDTO( //리뷰 등록 DTO
        @JsonProperty("order_id")
        Long orderId,
        Float rating,
        String comment) {
    public Review toEntity(Order order){ //DTO -> 리뷰 객체
        return Review.builder()
                .rating(rating)
                .comment(comment)
                .order(order)
                .build();
    }
}
