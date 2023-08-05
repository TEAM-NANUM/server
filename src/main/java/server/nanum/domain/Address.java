package server.nanum.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String zipCode;
    private String defaultAddress;
    private String detailAddress;
}
