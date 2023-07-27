package server.nanum.dto.Response;

import lombok.Builder;

@Builder
public record SimpleOrderDto(
        Long orderId,
        String orderName,
        String imgUrl){
}
