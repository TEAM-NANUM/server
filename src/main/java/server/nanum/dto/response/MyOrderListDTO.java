package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import server.nanum.domain.Order;

import java.util.List;
@JsonPropertyOrder({"count","orderList"})
public record MyOrderListDTO(
        @Schema(example = "1",description ="자신의 주문 개수" )
        Integer count,
        @Schema(description = "주문 정보")
        @JsonProperty("order_list")
        List<MyOrderDTO> orderList) {

        public static MyOrderListDTO toEntity(List<Order> orderList){
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
                return new MyOrderListDTO(DtoList.size(),DtoList);
        }
}
