package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.nanum.domain.Order;

import java.util.List;

public record MyCompleteOrdersDTO(
        Integer count,
        @JsonProperty("complete_orders")
        List<MyOrderDTO> completeOrders) {

        public static MyCompleteOrdersDTO toEntity(List<Order> orderList){
                List<MyOrderDTO> DtoList = orderList.stream().map((order)-> { //주문 객체 -> 주문 조회 단건 DTO 정보로 변환
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
