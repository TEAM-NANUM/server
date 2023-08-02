package server.nanum.dto.response;

import server.nanum.domain.Delivery;

public record OrderUserInfoDTO(
        String userName,
        String phoneNumber,
        String defaultAddress,
        String detailAddress,
        int point) {
    public static OrderUserInfoDTO toEntity(Delivery delivery){
        return new OrderUserInfoDTO(
                delivery.getUser().getName(),
                delivery.getPhoneNumber(),
                delivery.getDefaultAddress(),
                delivery.getDetailAddress(),
                delivery.getUser().getUserGroupPoint());
    }
}
