package server.nanum.domain;

import java.util.HashMap;
import java.util.Map;

public class AddressContainer {
    private static final Map<String, Address> addressMap = new HashMap<>();

    public static Address getAddress(String zipCode, String defaultAddress, String detailAddress) {
        String addressKey = zipCode + "-" + defaultAddress + "-" + detailAddress;

        if (addressMap.containsKey(addressKey)) {
            return addressMap.get(addressKey);
        } else {
            Address newAddress = Address.builder()
                    .zipCode(zipCode)
                    .defaultAddress(defaultAddress)
                    .detailAddress(detailAddress)
                    .build();
            addressMap.put(addressKey, newAddress);
            return newAddress;
        }
    }

    public static void addAddress(Address address) {
        String addressKey = address.getZipCode() + "-" + address.getDefaultAddress() + "-" + address.getDetailAddress();
        addressMap.put(addressKey, address);
    }
}
