package server.nanum.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Address {
    private String zipCode;
    private String defaultAddress;
    private String detailAddress;
}
