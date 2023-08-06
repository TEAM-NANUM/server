package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.nanum.domain.product.Product;
import java.util.List;

public record SellerProductsDTO(
        Integer count,
        List<SellerProductOneDTO> products) {

    public record SellerProductOneDTO( //판매자 제품 단건 정보 DTO
            @JsonProperty("product_id")
            Long productId,
            String name,
            @JsonProperty("img_url")
            String imgUrl,
            Integer unit,
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
