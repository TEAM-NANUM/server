package server.nanum.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Delivery;
import server.nanum.domain.Seller;
import server.nanum.domain.User;
import server.nanum.dto.user.request.GuestSignupDTO;
import server.nanum.dto.user.request.SellerSignupDTO;
import server.nanum.exception.ConflictException;
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
    private final EntityManager entityManager;

    /**
     * 게스트 회원가입 처리
     *
     * @param user 현재 사용자 정보
     * @param guestSignupDTO 게스트 회원가입 요청 DTO
     */
    public void registerGuest(User user, GuestSignupDTO guestSignupDTO) {
        User guest = guestSignupDTO.toGuest(user.getUserGroup());
        entityManager.persist(guest);

        Delivery newDelivery = guestSignupDTO.toDelivery(guest);
        deliveryRepository.save(newDelivery);
    }

    /**
     * 판매자 회원가입 처리
     *
     * @param sellerSignupDTO 판매자 회원가입 요청 DTO
     */
    public void registerSeller(SellerSignupDTO sellerSignupDTO) {
        checkDuplicateEmailAndPhoneNumber(sellerSignupDTO);

        Seller seller = sellerSignupDTO.toSeller();

        seller.withPoint(0L);

        seller.withEncryptedPassword(passwordEncoder.encode(seller.getPassword()));

        sellerRepository.save(seller);
    }

    private void checkDuplicateEmailAndPhoneNumber(SellerSignupDTO sellerSignupDTO) {
        if (sellerRepository.existsByEmail(sellerSignupDTO.getEmail())) {
            throw new ConflictException("이미 사용 중인 이메일입니다.");
        }

        if (sellerRepository.existsByPhoneNumber(sellerSignupDTO.getPhoneNumber())) {
            throw new ConflictException("이미 사용 중인 전화번호입니다.");
        }
    }
}


