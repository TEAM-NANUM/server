package server.nanum.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
