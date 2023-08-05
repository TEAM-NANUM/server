package server.nanum.dto.response;

import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;
import server.nanum.domain.product.Product;

import java.util.List;
public record SellerOrdersDTO(
        SellerOrdersInfoDTO products,
        List<SellerOrderOneDTO> orders){
    public record SellerOrderOneDTO(
            Long id,
            Integer quantity,
            String userName,
            DeliveryStatus deliveryStatus) {
    }

    public record SellerOrdersInfoDTO(
            String name,
            Integer unit,
            Integer price) {
    }
    public static SellerOrdersDTO toEntity(Product product, List<Order> orderList){
        List<SellerOrderOneDTO> orderDtoList = orderList.stream().map((order)-> {
            return new SellerOrderOneDTO(
                    order.getId(),
                    order.getProductCount(),
                    order.getUser().getName(),
                    order.getDeliveryStatus());
        }).toList();
        SellerOrdersInfoDTO productInfoDto = new SellerOrdersInfoDTO(
                product.getName(),
                product.getUnit(),
                product.getPrice());
        return new SellerOrdersDTO(productInfoDto,orderDtoList);
    }
}
