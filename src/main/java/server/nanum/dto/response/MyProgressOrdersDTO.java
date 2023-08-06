package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.nanum.domain.Order;

import java.util.List;

public record MyProgressOrdersDTO(
        Integer count,
        @JsonProperty("progress_orders")
        List<MyOrderDTO> progressOrders) {

    public static MyProgressOrdersDTO toEntity(List<Order> orderList){
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
        return new MyProgressOrdersDTO(DtoList.size(),DtoList);
    }

}
