package server.nanum.dto.response;

import server.nanum.domain.Order;
import server.nanum.domain.product.Product;

import java.util.List;
public record SellerOrdersDto(
        SellerOrdersInfoDto products,
        List<SellerOrderOneDto> orders){
    public static SellerOrdersDto toEntity(Product product,List<Order> orderList){
        List<SellerOrderOneDto> orderDtoList = orderList.stream().map((order)-> {
            return new SellerOrderOneDto(
                    order.getId(),
                    order.getProductCount(),
                    order.getUser().getName(),
                    order.getDeliveryStatus());
        }).toList();
        SellerOrdersInfoDto productInfoDto = new SellerOrdersInfoDto(
                product.getName(),
                product.getUnit(),
                product.getPrice());
        return new SellerOrdersDto(productInfoDto,orderDtoList);
    }
}
