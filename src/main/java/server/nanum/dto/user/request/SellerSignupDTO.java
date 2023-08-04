package server.nanum.dto.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import server.nanum.dto.request.AddressDTO;

@Getter
public class SellerSignupDTO extends AddressDTO {
    private String username;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String email;
    private String password;

    public AddressDTO getAddressDTO() {
        return new AddressDTO();
    }
}

