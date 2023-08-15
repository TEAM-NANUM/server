package server.nanum.dto.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import server.nanum.domain.Address;
import server.nanum.domain.Seller;
import server.nanum.dto.request.AddressDTO;

/**
 * 판매자 회원가입 요청 DTO
 * 판매자 회원가입에 필요한 정보를 담고 있습니다.
 * AddressDTO를 상속받아 주소 정보를 포함하고 있습니다.
 *
 * @see Seller
 * @since 2023-08-05
 **@author hyunjin
 */
@Getter
@Builder
@JsonPropertyOrder({"username", "email", "password", "phone_number", "address"})
@NoArgsConstructor
@AllArgsConstructor
public class SellerSignupDTO {
    @Schema(example = "나눔이",description = "판매자명")
    @NotBlank(message = "사용자명은 비어있을 수 없습니다!")
    private String username;

    @NotBlank(message = "전화번호를 입력해주세요!")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "유효한 전화번호 형식이 아닙니다!")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Schema(example = "user@example.com",description = "판매자 이메일")
    @NotBlank(message = "이메일은 비어있을 수 없습니다!")
    @Email(message = "유효한 이메일 주소가 아닙니다!")
    private String email;

    @Schema(example = "password123",description = "판매자 계정 비밀번호")
    @NotBlank(message = "비밀번호는 비어있을 수 없습니다!")
    private String password;

    @Valid
    @Schema(description = "판매자 주소")
    private AddressDTO address;

    /**
     * SellerSignupDTO 객체로부터 Seller 객체를 빌더를 사용하여 생성합니다.
     *
     * @return Seller 생성된 판매자 객체
     */
    public Seller toSeller() {
        Address address = this.address.toAddress();

        return Seller.builder()
                .name(username)
                .phoneNumber(phoneNumber)
                .email(email)
                .password(password)
                .address(address)
                .build();
    }
}



