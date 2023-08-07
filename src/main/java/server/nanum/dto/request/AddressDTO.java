package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.nanum.domain.Address;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    @JsonProperty("zip_code")
    private String zipCode;

    @JsonProperty("default_address")
    private String defaultAddress;

    @JsonProperty("detail_address")
    private String detailAddress;

    public String toString(){
        return this.zipCode.toString()+this.defaultAddress+this.detailAddress;
    }

    // 새로운 메서드 추가: AddressDTO를 Address로 변환하는 메서드
    public Address toAddress() {
        return Address.builder()
                .zipCode(this.zipCode)
                .defaultAddress(this.defaultAddress)
                .detailAddress(this.detailAddress)
                .build();
    }
}
