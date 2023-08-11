package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import server.nanum.domain.Order;
import server.nanum.domain.Review;
public record AddReviewDTO( //리뷰 등록 DTO
        @NotNull
        @JsonProperty("order_id")
        Long orderId,
        @NotNull
        @Positive
        @Max(value=5)
        Float rating,
        @NotNull
        String comment) {
    public Review toEntity(Order order){ //DTO -> 리뷰 객체
        return Review.builder()
                .rating(rating)
                .comment(comment)
                .order(order)
                .build();
    }
}
