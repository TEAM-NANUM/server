package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.nanum.domain.Order;

import java.util.List;

public record MyUnReviewOrdersDTO(
        Integer count,
        List<MyUnReviewDTO> orders){

    public record MyUnReviewDTO( //리뷰 안달린 주문 단건 정보 DTO
            Long id,
            String name,
            @JsonProperty("img_url")
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
