package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.nanum.domain.Delivery;
import server.nanum.dto.request.AddressDTO;

public record OrderUserInfoDTO( //주문시 유저 정보 불러오기 DTO
        @JsonProperty("user_name")
        String userName,
        @JsonProperty("phone_number")
        String phoneNumber,
        AddressDTO address,
        int point) {
    public static OrderUserInfoDTO toEntity(Delivery delivery){  //주소 객체 -> 유저 정보 DTO
        AddressDTO dto= AddressDTO.builder()
                .zipCode(delivery.getAddress().getZipCode())
                .defaultAddress(delivery.getAddress().getDefaultAddress())
                .defaultAddress(delivery.getAddress().getDetailAddress())
                .build();
        return new OrderUserInfoDTO(
                delivery.getUser().getName(),
                delivery.getPhoneNumber(),
                dto,
                delivery.getUser().getUserGroupPoint());
    }
}
