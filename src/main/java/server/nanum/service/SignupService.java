package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Delivery;
import server.nanum.domain.Seller;
import server.nanum.domain.User;
import server.nanum.dto.user.request.SellerSignupDTO;
import server.nanum.dto.user.request.GuestSignupDTO;
import server.nanum.repository.DeliveryRepository;
import server.nanum.repository.SellerRepository;

/**
 * SignupService는 회원가입 관련 비즈니스 로직을 처리하는 서비스입니다.
 * 게스트 회원가입과 판매자 회원가입을 처리하는 메서드를 제공합니다.
 * 회원가입 정보를 사용하여 User, Delivery, Seller 등의 엔티티를 생성하고 저장합니다.
 *@author hyunjin
 * @version 1.0.0
 * @since 2023-08-05
 */
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

    /**
     * 판매자 회원가입 처리
     *
     * @param sellerSignupDTO 판매자 회원가입 요청 DTO
     */
    public void registerSeller(SellerSignupDTO sellerSignupDTO) {
        Seller seller = sellerSignupDTO.toSeller();

        // point를 0으로 초기화
        seller.withPoint(0L);

        // password를 암호화
        seller.withEncryptedPassword(passwordEncoder.encode(seller.getPassword()));

        sellerRepository.save(seller);
    }
}


