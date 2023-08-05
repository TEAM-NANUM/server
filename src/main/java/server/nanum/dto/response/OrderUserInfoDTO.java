package server.nanum.dto.response;

import server.nanum.domain.Delivery;

public record OrderUserInfoDTO( //주문시 유저 정보 불러오기 DTO
        String userName,
        String phoneNumber,
        String defaultAddress,
        String detailAddress,
        int point) {
    public static OrderUserInfoDTO toEntity(Delivery delivery){  //주소 객체 -> 유저 정보 DTO
        return new OrderUserInfoDTO(
                delivery.getUser().getName(),
                delivery.getPhoneNumber(),
                delivery.getAddress().getDefaultAddress(),
                delivery.getAddress().getDetailAddress(),
                delivery.getUser().getUserGroupPoint());
    }
}
