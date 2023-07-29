package server.nanum.dto.Response;

import server.nanum.domain.Order;

import java.util.List;

public record MyUnReviewOrdersDto(
        Integer count,
        List<MyReviewDto> dto){
    public static MyUnReviewOrdersDto toEntity(List<Order> orderList){
        List<MyReviewDto> DtoList = orderList.stream().map((order)-> {
            return MyReviewDto.builder()
                    .orderId(order.getId())
                    .orderName(order.getProduct().getName())
                    .imgUrl(order.getProduct().getImgUrl())
                    .rating(order.getReview().getRating())
                    .comment(order.getReview().getComment())
                    .build();
        }).toList();
        return new MyUnReviewOrdersDto(DtoList.size(),DtoList);
    }

}
