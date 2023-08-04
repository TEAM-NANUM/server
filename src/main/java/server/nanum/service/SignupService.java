package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Delivery;
import server.nanum.domain.User;
import server.nanum.dto.user.response.GuestDTO;
import server.nanum.repository.DeliveryRepository;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SignupService {

    private final DeliveryRepository deliveryRepository;

    /**
     * 게스트 회원가입 처리
     *
     * @param user 현재 사용자 정보
     * @param guestDTO 게스트 회원가입 요청 DTO
     */
    public void registerGuestUser(User user, GuestDTO guestDTO) {
        // 게스트 정보를 사용자 정보로 변환
        User guest = guestDTO.toGuest(user.getUserGroup());

        // 새로운 배송 정보 생성 및 저장
        Delivery newDelivery = guestDTO.toDelivery(guest);
        deliveryRepository.save(newDelivery);
    }
}

