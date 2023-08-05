package server.nanum.dto.response;

import server.nanum.domain.product.Product;
import java.util.List;

public record SellerProductsDTO(
        Integer count,
        List<SellerProductOneDTO> products) {

    public record SellerProductOneDTO( //판매자 제품 단건 정보 DTO
            Long productId,
            String name,
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
