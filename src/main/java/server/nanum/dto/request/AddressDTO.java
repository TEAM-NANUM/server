package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import server.nanum.domain.Address;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    @Schema(example = "123-456",description = "우편번호")
    @NotBlank(message = "우편번호를 입력해주세요!")
    @JsonProperty("zip_code")
    private String zipCode;

    @Schema(example = "서울시 강남구",description = "기본 주소")
    @NotBlank(message = "기본 주소를 입력해주세요!")
    @JsonProperty("default_address")
    private String defaultAddress;

    @Schema(example = "삼성동 123번지",description = "상세 주소")
    @NotBlank(message = "상세 주소를 입력해주세요!")
    @JsonProperty("detail_address")
    @Length(max = 254,message = "상세 주소의 길이 제한을 넘었습니다")
    private String detailAddress;

    public String toString(){
        return this.zipCode.toString()+" "+this.defaultAddress+" "+this.detailAddress;
    }

    public Address toAddress() {
        Address address = Address.builder()
                .zipCode(this.zipCode)
                .defaultAddress(this.defaultAddress)
                .detailAddress(this.detailAddress)
                .build();

        return address;
    }
}
