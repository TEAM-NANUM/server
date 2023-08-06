package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.nanum.domain.Order;
import java.util.List;

public record MyReviewOrdersDTO(
        Integer count,
        List<MyReviewDTO> orders){
    public record MyReviewDTO( //리뷰 달린 주문 단건 정보 DTO
            Long id,
            String name,
            @JsonProperty("img_url")
            String imgUrl,
            Float rating,
            String comment){

    }
    public static MyReviewOrdersDTO toEntity(List<Order> orderList){
        List<MyReviewDTO> DtoList = orderList.stream().map((order)-> { //주문 정보  -> 주문 단건 정보 DTO로 변환
            return new MyReviewDTO(
                    order.getId(),
                    order.getProduct().getName(),
                    order.getProduct().getImgUrl(),
                    order.getReview().getRating(),
                    order.getReview().getComment());
        }).toList();
        return new MyReviewOrdersDTO(DtoList.size(),DtoList);
    }

}
