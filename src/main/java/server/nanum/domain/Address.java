package server.nanum.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Objects;

@Getter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String zipCode;
    private String defaultAddress;
    private String detailAddress;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(zipCode, address.zipCode) &&
                Objects.equals(defaultAddress, address.defaultAddress) &&
                Objects.equals(detailAddress, address.detailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipCode, defaultAddress, detailAddress);
    }
}
