package server.nanum.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 주소(Address) 정보를 관리하는 컨테이너 클래스입니다.
 * 이 클래스는 주소 정보를 관리하고, 동일한 주소 정보에 대한 중복 생성을 방지하여
 * 효율적인 메모리 사용을 도와줍니다.
 *
 * @version 1.0.0
 * @since 2023-08-15
 * @author hyunjin
 */
public class AddressContainer {

    private static final Map<String, Address> addressMap = new ConcurrentHashMap<>();

    public static Address getAddress(String zipCode, String defaultAddress, String detailAddress) {
        String addressKey = zipCode + "-" + defaultAddress + "-" + detailAddress;
        return addressMap.get(addressKey);
    }

    public static void addAddress(Address address) {
        String addressKey = address.getZipCode() + "-" + address.getDefaultAddress() + "-" + address.getDetailAddress();
        addressMap.put(addressKey, address);
    }

    public static void removeAddress(Address address) {
        String addressKey = address.getZipCode() + "-" + address.getDefaultAddress() + "-" + address.getDetailAddress();
        addressMap.remove(addressKey);
    }
}
