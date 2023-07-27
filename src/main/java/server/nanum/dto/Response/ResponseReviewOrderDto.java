package server.nanum.dto.Response;

import server.nanum.domain.Order;

import java.util.List;

public record ResponseReviewOrderDto(
        Integer count,
        List<SimpleOrderDto> dto){
    public static ResponseReviewOrderDto toEntity(List<Order> orderList){
        List<SimpleOrderDto> DtoList = orderList.stream().map((order)-> {
            return SimpleOrderDto.builder()
                    .orderId(order.getId())
                    .orderName(order.getProduct().getName())
                    .imgUrl(order.getProduct().getImgUrl())
                    .build();
        }).toList();
        return new ResponseReviewOrderDto(DtoList.size(),DtoList);
    }
}
