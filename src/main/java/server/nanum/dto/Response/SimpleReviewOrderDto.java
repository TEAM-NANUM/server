package server.nanum.dto.Response;

import lombok.Builder;

@Builder
public record SimpleReviewOrderDto(
        Long orderId,
        String orderName,
        String imgUrl,
        Float rating,
        String comment){

}
