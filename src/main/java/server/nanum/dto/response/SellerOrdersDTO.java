package server.nanum.dto.response;

import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;
import server.nanum.domain.product.Product;

import java.util.List;
public record SellerOrdersDTO(
        SellerOrdersInfoDTO products,
        List<SellerOrderOneDTO> orders){
    public record SellerOrderOneDTO( //판매자 주문 단건 정보
            Long id,
            Integer quantity,
            String userName,
            DeliveryStatus deliveryStatus) {
    }

    public record SellerOrdersInfoDTO( //판매자 제품 정보
            String name,
            Integer unit,
            Integer price) {
    }
    public static SellerOrdersDTO toEntity(Product product, List<Order> orderList){
        List<SellerOrderOneDTO> orderDtoList = orderList.stream().map((order)-> { //주문 객체 -> 판매자 주문 단건 정보 DTO
            return new SellerOrderOneDTO(
                    order.getId(),
                    order.getProductCount(),
                    order.getUser().getName(),
                    order.getDeliveryStatus());
        }).toList();
        SellerOrdersInfoDTO productInfoDto = new SellerOrdersInfoDTO( //제품 객체 -> 판매자 제품 정보 DTO
                product.getName(),
                product.getUnit(),
                product.getPrice());
        return new SellerOrdersDTO(productInfoDto,orderDtoList);
    }
}
