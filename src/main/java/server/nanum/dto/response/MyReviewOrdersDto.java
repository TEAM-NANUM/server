package server.nanum.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import server.nanum.domain.Order;

import java.util.List;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record MyReviewOrdersDto(
        Integer count,
        List<MyReviewDto> orders){
    public static MyReviewOrdersDto toEntity(List<Order> orderList){
        List<MyReviewDto> DtoList = orderList.stream().map((order)-> {
            return MyReviewDto.builder()
                    .id(order.getId())
                    .name(order.getProduct().getName())
                    .imgUrl(order.getProduct().getImgUrl())
                    .rating(order.getReview().getRating())
                    .comment(order.getReview().getComment())
                    .build();
        }).toList();
        return new MyReviewOrdersDto(DtoList.size(),DtoList);
    }

}
