package server.nanum.dto.response;

import server.nanum.domain.Order;
import java.util.List;

public record MyReviewOrdersDTO(
        Integer count,
        List<MyReviewDTO> orders){
    public record MyReviewDTO(
            Long id,
            String name,
            String imgUrl,
            Float rating,
            String comment){

    }
    public static MyReviewOrdersDTO toEntity(List<Order> orderList){
        List<MyReviewDTO> DtoList = orderList.stream().map((order)-> {
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
