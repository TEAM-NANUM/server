package server.nanum.dto.response;

import server.nanum.domain.Order;
import java.util.List;

public record MyUnReviewOrdersDTO(
        Integer count,
        List<MyUnReviewDTO> orders){

    public record MyUnReviewDTO(
            Long id,
            String name,
            String imgUrl){
    }

    public static MyUnReviewOrdersDTO toEntity(List<Order> orderList){
        List<MyUnReviewDTO> DtoList = orderList.stream().map((order)-> {
            return new MyUnReviewDTO(
                    order.getId(),
                    order.getProduct().getName(),
                    order.getProduct().getImgUrl());
        }).toList();
        return new MyUnReviewOrdersDTO(DtoList.size(),DtoList);
    }
}
