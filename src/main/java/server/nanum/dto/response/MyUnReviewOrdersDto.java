package server.nanum.dto.response;

import server.nanum.domain.Order;
import java.util.List;

public record MyUnReviewOrdersDto(
        Integer count,
        List<MyUnReviewDto> orders){
    public static MyUnReviewOrdersDto toEntity(List<Order> orderList){
        List<MyUnReviewDto> DtoList = orderList.stream().map((order)-> {
            return new MyUnReviewDto(
                    order.getId(),
                    order.getProduct().getName(),
                    order.getProduct().getImgUrl());
        }).toList();
        return new MyUnReviewOrdersDto(DtoList.size(),DtoList);
    }
}
