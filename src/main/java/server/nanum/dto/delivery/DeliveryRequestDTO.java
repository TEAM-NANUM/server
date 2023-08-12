package server.nanum.dto.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import server.nanum.domain.Delivery;
import server.nanum.domain.User;
import server.nanum.dto.request.AddressDTO;

@Getter
@Setter
public class DeliveryRequestDTO {
    @NotBlank(message = "수신자 이름을 입력해주세요!")
    private String receiver;

    @NotBlank(message = "별명을 입력해주세요!")
    private String nickname;

    @NotBlank(message = "전화번호를 입력해주세요!")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "유효한 전화번호 형식이 아닙니다!")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Valid
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

