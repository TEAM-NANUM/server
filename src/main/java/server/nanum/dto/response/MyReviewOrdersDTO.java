package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import server.nanum.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
@JsonPropertyOrder({"count","orders"})
public record MyReviewOrdersDTO(
        @Schema(example = "1",description ="조회한 주문 개수" )
        Integer count,
        @Schema(description = "주문 정보")
        List<MyReviewDTO> orders){
    @JsonPropertyOrder({"id","name","imgUrl","rating","comment"})
    public record MyReviewDTO( //리뷰 달린 주문 단건 정보 DTO
            @Schema(example = "1",description = "주문 번호")
            Long id,
            @Schema(example = "토마토",description = "주문 상품 이름")
            String name,
            @JsonProperty("img_url")
            @Schema(description = "주문 상품 대표이미지")
            String imgUrl,
            @Schema(example = "9999-99-99 99:99:99.999999",description = "주문 생성 날짜")
            @JsonProperty("order_created_at")
            LocalDateTime order_createdAt,
            @Schema(example = "5.0",description = "자신이 남긴 별점")
            Float rating,
            @Schema(example = "음식이 맛있어요",description = "자신이 작성한 후기")
            String comment,
            @Schema(example = "9999-99-99 99:99:99.999999",description = "리뷰 생성 날짜")
            @JsonProperty("review_created_at")
            LocalDateTime reviewCreatedAt){

    }
    public static MyReviewOrdersDTO toEntity(List<Order> orderList){
        List<MyReviewDTO> DtoList = orderList.stream().map((order)-> { //주문 정보  -> 주문 단건 정보 DTO로 변환
            return new MyReviewDTO(
                    order.getId(),
                    order.getProduct().getName(),
                    order.getProduct().getImgUrl(),
                    order.getCreateAt(),
                    order.getReview().getRating(),
                    order.getReview().getComment(),
                    order.getReview().getCreateAt());
        }).toList();
        return new MyReviewOrdersDTO(DtoList.size(),DtoList);
    }

}
