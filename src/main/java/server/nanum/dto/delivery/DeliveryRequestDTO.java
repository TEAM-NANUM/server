package server.nanum.dto.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.nanum.domain.Address;
import server.nanum.domain.AddressContainer;
import server.nanum.domain.Delivery;
import server.nanum.domain.User;
import server.nanum.dto.request.AddressDTO;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"receiver","nickname","phoneNumber","address"})
public class DeliveryRequestDTO {
    @Schema(example = "나눔이",description = "수신자명")
    @NotBlank(message = "수신자 이름을 입력해주세요!")
    private String receiver;

    @Schema(example = "집",description = "주소지 별칭")
    @NotBlank(message = "주소지 별칭을 입력해주세요!")
    private String nickname;

    @NotBlank(message = "전화번호를 입력해주세요!")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "유효한 전화번호 형식이 아닙니다!")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Valid
    @JsonProperty("address")
    @Schema(description = "배송지 주소")
    private AddressDTO addressDTO;

    public Delivery toEntity(User user) {
        Address addressEntity = Address.builder()
                .zipCode(addressDTO.getZipCode())
                .defaultAddress(addressDTO.getDefaultAddress())
                .detailAddress(addressDTO.getDetailAddress())
                .build();
        AddressContainer.addAddress(addressEntity);

        return Delivery.builder()
                .receiver(receiver)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .address(addressEntity)
                .user(user)
                .build();
    }
}