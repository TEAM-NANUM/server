package server.nanum.dto.response;

import server.nanum.domain.product.Product;
import java.util.List;

public record SellerProductsDto(
        Integer count,
        List<SellerProductOneDto> products) {
    public static SellerProductsDto toEntity(List<Product> productList){
        List<SellerProductOneDto> dtoList = productList.stream().map((product)-> {
            return new SellerProductOneDto(
                    product.getId(),
                    product.getName(),
                    product.getImgUrl(),
                    product.getUnit(),
                    product.getPrice());
        }).toList();
        return new SellerProductsDto(dtoList.size(),dtoList);
    }

}
