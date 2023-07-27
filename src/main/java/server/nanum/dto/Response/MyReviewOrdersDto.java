package server.nanum.dto.Response;

import server.nanum.domain.Order;

import java.util.List;

public record MyReviewOrdersDto(
        Integer count,
        List<MyUnReviewDto> dto){
    public static MyReviewOrdersDto toEntity(List<Order> orderList){
        List<MyUnReviewDto> DtoList = orderList.stream().map((order)-> {
            return MyUnReviewDto.builder()
                    .orderId(order.getId())
                    .orderName(order.getProduct().getName())
                    .imgUrl(order.getProduct().getImgUrl())
                    .build();
        }).toList();
        return new MyReviewOrdersDto(DtoList.size(),DtoList);
    }
}
