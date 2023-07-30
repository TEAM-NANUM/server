package server.nanum.domain.dto.Response;

import lombok.Builder;

@Builder
public record MyUnReviewDto(
        Long orderId,
        String orderName,
        String imgUrl){
}
