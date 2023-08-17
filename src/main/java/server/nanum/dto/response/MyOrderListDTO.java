package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;

import java.util.List;
@JsonPropertyOrder({"count","orderList"})
public record MyOrderListDTO(
        @Schema(example = "1",description ="자신의 주문 개수" )
        Integer count,
        @Schema(description = "주문 정보")
        @JsonProperty("order_list")
        List<MyOrderDTO> orderList) {
        @JsonPropertyOrder({"orderId","customer","name","deliveryStatus","totalPrice","unit","quantity"})
        public record MyOrderDTO( //주문조회 단건 정보
                @JsonProperty("order_id")
                @Schema(example = "1",description = "주문 번호")
                Long orderId,
                @Schema(example="나눔이",description = "주문한 사용자명")
                String customer,
                @Schema(example = "1000",description = "총 주문 가격")
                @JsonProperty("total_price")
                Integer totalPrice,
                @JsonProperty("delivery_status")
                @Schema(example = "IN_PROGRESS",description ="주문 상태")
                DeliveryStatus deliveryStatus,
                @Schema(example = "토마토",description = "주문 상품 이름")
                String name,
                @Schema(example = "10",description = "주문 상품 단위(Kg)")
                Integer unit,
                @Schema(example = "1",description = "주문 상품 개수")
                Integer quantity){
        }

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
