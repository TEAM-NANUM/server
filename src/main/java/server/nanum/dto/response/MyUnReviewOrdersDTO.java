package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import server.nanum.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
@JsonPropertyOrder({"count","orders"})
public record MyUnReviewOrdersDTO(
        @Schema(example = "1",description ="조회한 주문 개수" )
        Integer count,
        @Schema(description = "주문 정보")
        List<MyUnReviewDTO> orders){
    @JsonPropertyOrder({"id","name","imgUrl"})
    public record MyUnReviewDTO( //리뷰 안달린 주문 단건 정보 DTO
        @Schema(example = "1",description = "주문 번호")
        Long id,
        @Schema(example = "토마토",description = "주문 상품 이름")
        String name,
        @JsonProperty("img_url")
        @Schema(description = "주문 상품 대표이미지")
        String imgUrl,
        @Schema(example = "9999-99-99 99:99:99.999999",description = "주문 생성 날짜")
        @JsonProperty("order_created_at")
        LocalDateTime orderCreatedAt){
    }

    public static MyUnReviewOrdersDTO toEntity(List<Order> orderList){
        List<MyUnReviewDTO> DtoList = orderList.stream().map((order)-> {
            return new MyUnReviewDTO(
                    order.getId(),
                    order.getProduct().getName(),
                    order.getProduct().getImgUrl(),
                    order.getCreateAt());
        }).toList();
        return new MyUnReviewOrdersDTO(DtoList.size(),DtoList);
    }
}
