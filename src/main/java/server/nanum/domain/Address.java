package server.nanum.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String zipCode;
    private String defaultAddress;
    private String detailAddress;
}
