package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
@JsonPropertyOrder({"count","orderList"})
public record MyOrderListDTO(
        @Schema(example = "1",description ="자신의 주문 개수" )
        Integer count,
        @Schema(description = "주문 정보")
        @JsonProperty("order_list")
        List<MyOrderDTO> orderList) {
        @JsonPropertyOrder({"orderId","customer","name","createdAt","deliveryStatus","totalPrice","unit","quantity","imgUrl"})
        public record MyOrderDTO( //주문조회 단건 정보
                @JsonProperty("order_id")
                @Schema(example = "1",description = "주문 번호")
                Long orderId,
                @Schema(example="나눔이",description = "주문한 사용자명")
                String customer,
                @Schema(example = "1000",description = "총 주문 가격")
                @JsonProperty("total_price")
                Integer totalPrice,
                @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                @Schema(example = "9999-99-99T99:99:99:99",description = "주문 생성 날짜")
                @JsonProperty("created_at")
                LocalDateTime createdAt,
                @JsonProperty("delivery_status")
                @Schema(example = "IN_PROGRESS",description ="주문 상태")
                DeliveryStatus deliveryStatus,
                @Schema(example = "토마토",description = "주문 상품 이름")
                String name,
                @Schema(example = "10",description = "주문 상품 단위(Kg)")
                Integer unit,
                @Schema(example = "1",description = "주문 상품 개수")
                Integer quantity,
                @JsonProperty("img_url")
                @Schema(description = "주문 상품 대표이미지")
                String imgUrl){
        }

        public static MyOrderListDTO toEntity(List<Order> orderList){
                List<MyOrderDTO> DtoList = orderList.stream().map((order)-> { //주문 객체 -> 주문 조회 단건 DTO 정보로 변환
                        return new MyOrderDTO(
                                order.getId(),
                                order.getUser().getName(),
                                order.getTotalAmount(),
                                order.getCreateAt(),
                                order.getDeliveryStatus(),
                                order.getProduct().getName(),
                                order.getProduct().getUnit(),
                                order.getProductCount(),
                                order.getProduct().getImgUrl());
                }).toList();
                return new MyOrderListDTO(DtoList.size(),DtoList);
        }
}
