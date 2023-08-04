package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Address;
import server.nanum.domain.Delivery;
import server.nanum.domain.Seller;
import server.nanum.domain.User;
import server.nanum.dto.request.AddressDTO;
import server.nanum.dto.user.request.SellerSignupDTO;
import server.nanum.dto.user.request.GuestSignupDTO;
import server.nanum.repository.DeliveryRepository;
import server.nanum.repository.SellerRepository;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SignupService {

    private final DeliveryRepository deliveryRepository;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 게스트 회원가입 처리
     *
     * @param user 현재 사용자 정보
     * @param guestSignupDTO 게스트 회원가입 요청 DTO
     */
    public void registerGuest(User user, GuestSignupDTO guestSignupDTO) {
        // 게스트 정보를 사용자 정보로 변환
        User guest = guestSignupDTO.toGuest(user.getUserGroup());

        // 새로운 배송 정보 생성 및 저장
        Delivery newDelivery = guestSignupDTO.toDelivery(guest);
        deliveryRepository.save(newDelivery);
    }

    public void registerSeller(SellerSignupDTO sellerSignupDTO) {
        Seller seller = createSellerFromDTO(sellerSignupDTO);
        seller = seller.withPoint(0L); // point를 0으로 초기화
        seller = seller.withEncryptedPassword(passwordEncoder.encode(seller.getPassword())); // password를 암호화
        sellerRepository.save(seller); // Seller 엔티티 저장
    }

    private Seller createSellerFromDTO(SellerSignupDTO sellerSignupDTO) {
        AddressDTO addressDTO = sellerSignupDTO.getAddressDTO();
        Address address = new Address(addressDTO.getZipCode(), addressDTO.getDefaultAddress(), addressDTO.getDetailAddress());

        return Seller.builder()
                .name(sellerSignupDTO.getUsername())
                .phoneNumber(sellerSignupDTO.getPhoneNumber())
                .email(sellerSignupDTO.getEmail())
                .password(sellerSignupDTO.getPassword())
                .address(address)
                .build();
    }
}

