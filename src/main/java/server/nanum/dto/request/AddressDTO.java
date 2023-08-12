package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "우편번호를 입력해주세요.")
    @JsonProperty("zip_code")
    private String zipCode;

    @NotBlank(message = "기본 주소를 입력해주세요.")
    @JsonProperty("default_address")
    private String defaultAddress;

    @NotBlank(message = "상세 주소를 입력해주세요.")
    @JsonProperty("detail_address")
    private String detailAddress;

    public String toString(){
        return this.zipCode.toString()+" "+this.defaultAddress+" "+this.detailAddress;
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
