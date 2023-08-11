package server.nanum.dto.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import server.nanum.domain.Delivery;
import server.nanum.domain.User;
import server.nanum.dto.request.AddressDTO;

@Getter
@Setter
public class DeliveryRequestDTO {
    private String receiver;
    private String nickname;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private AddressDTO address;

    // DTO를 기반으로 Delivery 엔티티 객체를 생성하는 메서드
    public Delivery toEntity(User user) {
        return Delivery.builder()
                .receiver(receiver)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .address(address.toAddress())
                .user(user)
                .build();
    }
}

