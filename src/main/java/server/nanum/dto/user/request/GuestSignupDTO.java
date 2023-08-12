package server.nanum.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import server.nanum.domain.*;
import server.nanum.dto.request.AddressDTO;

import java.util.UUID;

/**
 * 게스트 회원가입 요청 DTO
 * 게스트 회원가입에 필요한 정보를 담고 있습니다.
 * AddressDTO를 상속받아 주소 정보를 포함하고 있습니다.
 *
 * @see User
 * @see Delivery
 **@author hyunjin
 * @since 2023-08-05
 */
@Getter
public class GuestSignupDTO {
    @NotBlank(message="게스트 명은 비어있을 수 없습니다!")
    @Schema(example = "나눔이")
    private String nickname;

    @Valid
    private AddressDTO address;

    /**
     * 게스트 정보를 사용자(User) 정보로 변환합니다.
     *
     * @param userGroup 사용자 그룹 정보
     * @return User 변환된 사용자 정보 객체
     */
    public User toGuest(UserGroup userGroup) {
        String shortUUID = UUID.randomUUID().toString().substring(0, 8);
        return User.builder()
                .name(nickname)
                .userRole(UserRole.GUEST)
                .userGroup(userGroup)
                .inviteCode(shortUUID)
                .build();
    }

    /**
     * 게스트의 정보를 바탕으로 배송 정보(Delivery)를 생성합니다.
     *
     * @param user 사용자 정보
     * @return Delivery 생성된 배송 정보 객체
     */
    public Delivery toDelivery(User user) {
        Address address = this.address.toAddress();

        return Delivery.builder()
                .nickname("기본")
                .phoneNumber(null)
                .address(address)
                .isDefault(true)
                .user(user)
                .build();
    }
}

