package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import server.nanum.domain.product.Product;

import java.util.List;
@JsonPropertyOrder({"count","products"})
public record SellerProductsDTO(
        @Schema(example = "1",description = "상품 개수")
        Integer count,
        @Schema(description = "상품 정보")
        List<SellerProductOneDTO> products) {

    @JsonPropertyOrder({"productId","name","imgUrl","unit","price"})
    public record SellerProductOneDTO( //판매 상품 단건 정보 DTO
            @JsonProperty("product_id")
            @Schema(example = "1",description = "상품 번호")
            Long productId,
            @Schema(example = "토마토",description = "상품 이름")
            String name,
            @JsonProperty("img_url")
            @Schema(description = "상품 대표 이미지")
            String imgUrl,
            @Schema(example = "10",description = "상품 단위(Kg)")
            Integer unit,
            @Schema(example = "1000",description = "상품 가격")
            Integer price) {
    }


    public static SellerProductsDTO toEntity(List<Product> productList){
        List<SellerProductOneDTO> dtoList = productList.stream().map((product)-> { //제품 객체 -> 판매자 제품 단건 정보 DTO
            return new SellerProductOneDTO(
                    product.getId(),
                    product.getName(),
                    product.getImgUrl(),
                    product.getUnit(),
                    product.getPrice());
        }).toList();
        return new SellerProductsDTO(dtoList.size(),dtoList);
    }

}
