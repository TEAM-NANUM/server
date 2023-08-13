package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import server.nanum.domain.Delivery;
import server.nanum.dto.request.AddressDTO;
@JsonPropertyOrder({"userName","phoneNumber","address"})
public record OrderUserInfoDTO( //주문시 유저 정보 불러오기 DTO
        @JsonProperty("user_name")
        @Schema(example = "나눔이",description = "사용자명")
        String userName,
        @JsonProperty("phone_number")
        @Schema(example = "010-1111-1111",description = "사용자 전화번호")
        String phoneNumber,
        @Schema(description = "사용자 기본 주소")
        AddressDTO address,
        @Schema(example = "10000",description = "사용자 포인트")
        int point) {
    public static OrderUserInfoDTO toEntity(Delivery delivery){  //주소 객체 -> 유저 정보 DTO
        AddressDTO dto= AddressDTO.builder()
                .zipCode(delivery.getAddress().getZipCode())
                .defaultAddress(delivery.getAddress().getDefaultAddress())
                .detailAddress(delivery.getAddress().getDetailAddress())
                .build();
        return new OrderUserInfoDTO(
                delivery.getUser().getName(),
                delivery.getPhoneNumber(),
                dto,
                delivery.getUser().getUserGroupPoint());
    }
}
