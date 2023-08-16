package server.nanum.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import server.nanum.domain.Order;
import server.nanum.domain.Review;

import java.util.List;

@JsonPropertyOrder({"count","reviews"})
public record AllReviewsDTO(
        @Schema(example = "1",description ="조회한 리뷰 개수" )
        Integer count,
        @Schema(description = "리뷰 정보")
        List<ReviewInfoDTO> reviews){
    @JsonPropertyOrder({"id","userName","productId","imgUrl","rating","comment"})
    public record ReviewInfoDTO( //리뷰 안달린 주문 단건 정보 DTO
                                 @Schema(example = "1",description = "리뷰 번호")
                                 Long id,
                                 @Schema(example = "나눔이",description = "작성자명")
                                 String userName,
                                 @Schema(example = "1",description = "상품 번호")
                                 Long productId,
                                 @JsonProperty("img_url")
                                 @Schema(description = "주문 상품 대표이미지")
                                 String imgUrl,
                                 @Schema(example = "5.0",description = "리뷰 별점")
                                 Float rating,
                                 @Schema(example = "음식이 맛있어요",description = "리뷰 내용")
                                 String comment){
    }

    public static AllReviewsDTO toEntity(List<Review> reviewList){
        List<ReviewInfoDTO> DtoList = reviewList.stream().map((review)-> {
            return new ReviewInfoDTO(
                    review.getId(),
                    review.getOrder().getUser().getName(),
                    review.getOrder().getProduct().getId(),
                    review.getOrder().getProduct().getImgUrl(),
                    review.getRating(),
                    review.getComment());
        }).toList();
        return new AllReviewsDTO(DtoList.size(),DtoList);
    }
}