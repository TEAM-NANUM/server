package server.nanum.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import server.nanum.domain.Order;
import server.nanum.domain.Review;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record AddReviewDTO( //리뷰 등록 DTO
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
