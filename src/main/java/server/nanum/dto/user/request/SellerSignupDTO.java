package server.nanum.dto.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
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
@JsonPropertyOrder({"username", "email", "password", "phone_number", "address"})
public class SellerSignupDTO {
    private String username;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String email;
    private String password;


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



