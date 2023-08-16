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

    public DeliveryListResponse getUserDeliveries(User user) {
        List<Delivery> deliveries = deliveryRepository.findByUser(user);
        return new DeliveryListResponse(toDeliveryResponses(deliveries));
    }

    @Transactional
    public void save(DeliveryRequestDTO request, User user) {
        Delivery delivery = request.toEntity(user);
        handleFirstDelivery(user, delivery);
        deliveryRepository.save(delivery);
    }

    @Transactional
    public void toggleDefaultStatus(Long id, User user) {
        Delivery delivery = findByIdAndUser(id, user);
        updateDefaultStatus(delivery, user);
    }

    @Transactional
    public void update(Long id, DeliveryRequestDTO request, User user) {
        Delivery delivery = findByIdAndUser(id, user);
        delivery.update(request.getReceiver(), request.getNickname(), request.getPhoneNumber(), request.getAddressDTO().toAddress());
    }

    @Transactional
    public void delete(Long id, User user) {
        Delivery delivery = findByIdAndUser(id, user);
        handleDefaultDeletion(delivery, user);
        deliveryRepository.delete(delivery);
    }

    private List<DeliveryResponse> toDeliveryResponses(List<Delivery> deliveries) {
        return deliveries.stream().map(DeliveryResponse::fromEntity).collect(Collectors.toList());
    }

    private void handleFirstDelivery(User user, Delivery delivery) {
        if (deliveryRepository.countByUser(user) == 0) {
            delivery.makeDefault();
        }
    }

    private void updateDefaultStatus(Delivery delivery, User user) {
        if (delivery.isDefault()) {
            delivery.resetDefault();
        } else {
            resetDefault(user);
            delivery.makeDefault();
        }
    }

    private void handleDefaultDeletion(Delivery delivery, User user) {
        if (delivery.isDefault()) {
            List<Delivery> deliveries = deliveryRepository.findByUser(user);

            if (!deliveries.isEmpty()) {
                deliveries.get(0).makeDefault();
            }
        }
    }

    private Delivery findByIdAndUser(Long id, User user) {
        return deliveryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NotFoundException("배송지 정보가 존재하지 않습니다."));
    }

    private void resetDefault(User user) {
        deliveryRepository.findByUserAndIsDefaultTrue(user)
                .ifPresent(Delivery::resetDefault);
    }
}


