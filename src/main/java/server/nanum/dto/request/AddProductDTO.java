package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.nanum.domain.DeliveryType;
import server.nanum.domain.product.Product;
import server.nanum.domain.Seller;
import server.nanum.domain.product.SubCategory;

public record AddProductDTO( //제품 등록 DTO
        String name,
        Integer price,
        Integer unit,
        String description,
        @JsonProperty("img_url")
        String imgUrl,
        @JsonProperty("delivery_type")
        DeliveryType deliveryType,
        @JsonProperty("subcategory_id")
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
                .build();
    }
}
