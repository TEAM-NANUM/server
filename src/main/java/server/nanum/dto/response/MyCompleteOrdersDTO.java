package server.nanum.dto.response;

import server.nanum.domain.Order;

import java.util.List;

public record MyCompleteOrdersDTO(
        Integer count,
        List<MyOrderDTO> completeOrders) {

        public static MyCompleteOrdersDTO toEntity(List<Order> orderList){
                List<MyOrderDTO> DtoList = orderList.stream().map((order)-> {
                        return new MyOrderDTO(
                                order.getId(),
                                order.getUser().getName(),
                                order.getTotalAmount(),
                                order.getDeliveryStatus(),
                                order.getProduct().getName(),
                                order.getProduct().getUnit(),
                                order.getProductCount());
                }).toList();
                return new MyCompleteOrdersDTO(DtoList.size(),DtoList);
        }
}
