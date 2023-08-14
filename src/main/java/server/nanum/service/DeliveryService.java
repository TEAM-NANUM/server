package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Delivery;
import server.nanum.domain.User;
import server.nanum.dto.delivery.DeliveryListResponse;
import server.nanum.dto.delivery.DeliveryListResponse.DeliveryResponse;
import server.nanum.dto.delivery.DeliveryRequestDTO;
import server.nanum.exception.NotFoundException;
import server.nanum.repository.DeliveryRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 배송지 관리 서비스
 * 사용자의 배송지 관련 로직을 처리합니다.
 *@author hyunjin
 * @version 1.0.0
 * @since 2023-08-05
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    /**
     * 사용자의 배송지 목록 조회
     *
     * @param user 현재 사용자 정보
     * @return DeliveryListResponse 사용자의 배송지 목록 응답
     */
    public DeliveryListResponse getDeliveryList(User user) {
        List<Delivery> deliveries = deliveryRepository.findByUser(user);
        List<DeliveryResponse> deliveryResponses = deliveries.stream()
                .map(DeliveryResponse::fromEntity)
                .collect(Collectors.toList());
        return new DeliveryListResponse(deliveryResponses);
    }

    /**
     * 새로운 배송지 정보 저장
     *
     * @param request 배송지 정보 요청 DTO
     * @param user 현재 사용자 정보
     */
    @Transactional
    public void saveDelivery(DeliveryRequestDTO request, User user) {
        Delivery delivery = request.toEntity(user);
        deliveryRepository.save(delivery);
    }

    /**
     * 선택된 배송지를 기본 배송지로 설정/해제
     *
     * @param id 변경하려는 배송지 ID
     * @param user 현재 사용자 정보
     */
    @Transactional
    public void toggleDefault(Long id, User user) {
        Delivery delivery = findDeliveryByIdAndUser(id, user);
        updateDefaultStatus(delivery, user);
    }

    /**
     * 사용자와 ID를 기반으로 배송지 정보 조회
     *
     * @param id 조회하려는 배송지 ID
     * @param user 현재 사용자 정보
     * @return Delivery 사용자의 특정 배송지 정보
     * @throws NotFoundException 해당 배송지 정보가 없을 경우 발생
     */
    private Delivery findDeliveryByIdAndUser(Long id, User user) {
        return deliveryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NotFoundException("배송지 정보가 존재하지 않습니다."));
    }


    /**
     * 선택된 배송지의 기본 배송지 상태 변경 (설정/해제)
     *
     * @param delivery 변경하려는 배송지 정보
     * @param user 현재 사용자 정보
     */
    private void updateDefaultStatus(Delivery delivery, User user) {
        if (delivery.isDefault()) {
            delivery.changeDefaultStatus(false);
        } else {
            resetExistingDefault(user);
            delivery.changeDefaultStatus(true);
        }
    }

    /**
     * 기존의 기본 배송지를 해제
     * 기존에 설정된 기본 배송지가 있다면 isDefault를 false로 설정합니다.
     *
     * @param user 현재 사용자 정보
     */
    private void resetExistingDefault(User user) {
        deliveryRepository.findByUserAndIsDefaultTrue(user)
                .ifPresent(existingDefault -> {existingDefault
                    .changeDefaultStatus(false);
        });
    }


    /**
     * 배송지 정보 업데이트
     *
     * @param id 배송지 아이디
     * @param request 업데이트할 배송지 정보
     * @param user 현재 사용자 정보
     */
    @Transactional
    public void updateDelivery(Long id, DeliveryRequestDTO request, User user) {
        Delivery delivery = findDeliveryByIdAndUser(id, user);
        delivery.updateDelivery(request.getReceiver(), request.getNickname(), request.getPhoneNumber(), request.getAddressDTO().toAddress());
    }

    /**
     * 배송지 정보 삭제
     *
     * @param id 삭제할 배송지 아이디
     * @param user 현재 사용자 정보
     */
    @Transactional
    public void deleteDelivery(Long id, User user) {
        Delivery delivery = findDeliveryByIdAndUser(id, user);
        deliveryRepository.delete(delivery);
    }
}

