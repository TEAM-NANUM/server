package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.boot.autoconfigure.graphql.ConditionalOnGraphQlSchema;
import server.nanum.domain.DeliveryStatus;
import server.nanum.domain.Order;
import server.nanum.domain.product.Product;

import java.time.LocalDateTime;
import java.util.List;
@JsonPropertyOrder({"products","inProgressCount","comPleteCount","orders"})
public record SellerOrdersDTO(
        @Schema(description = "상품 정보")
        SellerOrdersInfoDTO products,
        @Schema(example = "1",description = "진행중인 상품 개수")
        Integer inProgressOrderCount,
        @Schema(example = "0",description = "배송 종료된 상품 개수")
        Integer completeOrderCount,
        @Schema(description = "주문 정보")
        List<SellerOrderOneDTO> orders){
    @JsonPropertyOrder({"id","userName","quantity","deliveryStatus"})
    public record SellerOrderOneDTO( //상품의 주문 단건 정보
            @Schema(example = "1",description = "주문 번호")
            Long id,
            @Schema(example = "1",description = "상품 주문 개수")
            Integer quantity,
            @Schema(example = "나눔이",description = "주문한 사용자명")
            @JsonProperty("user_name")
            String userName,
            @Schema(example = "IN_PROGRESS",description = "주문 상태")
            @JsonProperty("delivery_status")
            DeliveryStatus deliveryStatus,
            @Schema(example = "9999-99-99 99:99:99.999999",description = "주문 생성 날짜")
            @JsonProperty("created_at")
            LocalDateTime createdAt){
    }
    @JsonPropertyOrder({"name","unit","price"})
    public record SellerOrdersInfoDTO( //판매 상품 정보
            @Schema(example = "토마토",description = "상품 이름")
            String name,
            @Schema(description = "상품 대표 이미지")
            @JsonProperty("img_url")
            String imgUrl,
            @Schema(example = "10",description = "상품 단위(Kg)")
            Integer unit,
            @Schema(example = "1000",description = "상품 가격")
            Integer price) {
    }
    public static SellerOrdersDTO toEntity(Product product, List<Order> orderList,Integer completeCount,Integer inProgressCount){
        List<SellerOrderOneDTO> orderDtoList = orderList.stream().map((order)-> { //주문 객체 -> 판매자 주문 단건 정보 DTO
            return new SellerOrderOneDTO(
                    order.getId(),
                    order.getProductCount(),
                    order.getUser().getName(),
                    order.getDeliveryStatus(),
                    order.getCreateAt());
        }).toList();
        SellerOrdersInfoDTO productInfoDto = new SellerOrdersInfoDTO( //제품 객체 -> 판매자 제품 정보 DTO
                product.getName(),
                product.getImgUrl(),
                product.getUnit(),
                product.getPrice());
        return new SellerOrdersDTO(productInfoDto,completeCount,inProgressCount,orderDtoList);
    }
}
