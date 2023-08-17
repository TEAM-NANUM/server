package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import server.nanum.domain.DeliveryStatus;

public record UpdateSellerOrderDTO(
        @NotNull(message = "배송 상태를 입력해주세요")
        @Schema(example = "DELIVERED",description = "배송 상태")
        @JsonProperty("delivery_status")
        DeliveryStatus deliveryStatus) {
}
