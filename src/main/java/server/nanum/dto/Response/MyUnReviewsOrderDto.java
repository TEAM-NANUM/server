package server.nanum.dto.Response;

import server.nanum.domain.Order;

import java.util.List;

public record MyUnReviewsOrderDto(
        Integer count,
        List<MyReviewDto> dto){
    public static MyUnReviewsOrderDto toEntity(List<Order> orderList){
        List<MyReviewDto> DtoList = orderList.stream().map((order)-> {
            return MyReviewDto.builder()
                    .orderId(order.getId())
                    .orderName(order.getProduct().getName())
                    .imgUrl(order.getProduct().getImgUrl())
                    .rating(order.getReview().getRating())
                    .comment(order.getReview().getComment())
                    .build();
        }).toList();
        return new MyUnReviewsOrderDto(DtoList.size(),DtoList);
    }

}
