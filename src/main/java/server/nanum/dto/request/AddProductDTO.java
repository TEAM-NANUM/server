package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import server.nanum.domain.DeliveryType;
import server.nanum.domain.Seller;
import server.nanum.domain.product.Product;
import server.nanum.domain.product.SubCategory;
@JsonPropertyOrder({"name","subCategoryId","imgUrl","description","price","unit","deliveryType"})
public record AddProductDTO( //상품 등록 DTO
        @NotBlank(message = "상품 이름을 입력해주세요")
        @Schema(example = "토마토",description = "상품 이름")
        String name,
        @NotBlank(message = "상품 가격을 입력해주세요")
        @Schema(example = "1000",description = "상품 가격")
        @PositiveOrZero(message = "상품 가격은 0 또는 양수만 가능합니다")
        Integer price,
        @Positive(message = "상품 무게는 양수만 가능합니다")
        @NotBlank(message = "상품 단위(무게)를 입력해주세요")
        @Schema(example = "10",description = "상품 단위(Kg)")
        Integer unit,
        @NotEmpty(message = "상품의 설명을 입력해주세요")
        @Schema(description = "상품 설명")
        String description,
        @NotBlank(message = "상품의 대표 이미지를 설정해주세요")
        @Schema(description = "상품 대표 이미지")
        @JsonProperty("img_url")
        String imgUrl,
        @NotBlank(message = "배송 방법을 결정해주세요")
        @JsonProperty("delivery_type")
        @Schema(example = "PACKAGE",defaultValue = "PACKAGE",description ="상품 배송 방법" )
        DeliveryType deliveryType,
        @NotBlank(message = "카테고리 번호를 입력해주세요")
        @JsonProperty("subcategory_id")
        @Positive(message = "카테고리 번호는 양수만 존재합니다")
        @Schema(example = "1",description = "카테고리 번호")
        Long subCategoryId) {
    public Product toEntity(Seller seller, SubCategory subCategory){ //DTO -> 제품 객체
        return Product.builder()
                .name(name)
                .price(price)
                .unit(unit)
                .description(description)
                .imgUrl(imgUrl)
                .deliveryType(deliveryType)
                .subCategory(subCategory)
                .seller(seller)
                .ratingAvg(0F)
                .viewCnt(0)
                .purchaseCnt(0)
                .reviewCnt(0)
                .build();
    }
}
