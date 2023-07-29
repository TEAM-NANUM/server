package server.nanum.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import server.nanum.domain.Order;

import java.util.List;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)

public record MyUnReviewOrdersDto(
        Integer count,
        List<MyUnReviewDto> orders){
    public static MyUnReviewOrdersDto toEntity(List<Order> orderList){
        List<MyUnReviewDto> DtoList = orderList.stream().map((order)-> {
            return MyUnReviewDto.builder()
                    .id(order.getId())
                    .name(order.getProduct().getName())
                    .imgUrl(order.getProduct().getImgUrl())
                    .build();
        }).toList();
        return new MyUnReviewOrdersDto(DtoList.size(),DtoList);
    }
}
