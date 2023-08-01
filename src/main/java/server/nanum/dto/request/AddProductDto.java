package server.nanum.dto.request;

import server.nanum.domain.DeliveryType;
import server.nanum.domain.product.Product;
import server.nanum.domain.Seller;
import server.nanum.domain.product.SubCategory;

public record AddProductDto(
        String name,
        Integer price,
        Integer unit,
        String description,
        String imgUrl,
        DeliveryType deliveryType,
        Long subCategoryId) {
    public Product toEntity(Seller seller, SubCategory subCategory){
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
