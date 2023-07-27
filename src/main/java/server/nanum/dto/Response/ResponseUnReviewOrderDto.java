package server.nanum.dto.Response;

import server.nanum.domain.Order;

import java.util.List;

public record ResponseUnReviewOrderDto(
        Integer count,
        List<SimpleReviewOrderDto> dto){
    public static ResponseUnReviewOrderDto toEntity(List<Order> orderList){
        List<SimpleReviewOrderDto> DtoList = orderList.stream().map((order)-> {
            return SimpleReviewOrderDto.builder()
                    .orderId(order.getId())
                    .orderName(order.getProduct().getName())
                    .imgUrl(order.getProduct().getImgUrl())
                    .rating(order.getReview().getRating())
                    .comment(order.getReview().getComment())
                    .build();
        }).toList();
        return new ResponseUnReviewOrderDto(DtoList.size(),DtoList);
    }

}
