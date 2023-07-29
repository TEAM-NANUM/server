package server.nanum.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import server.nanum.domain.Order;
import server.nanum.domain.Review;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record AddReviewDto(
        Long orderId,
        Float rating,
        String comment) {
    public Review toEntity(Order order){
        return Review.builder()
                .rating(rating)
                .comment(comment)
                .order(order)
                .build();
    }
}
