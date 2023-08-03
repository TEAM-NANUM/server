package server.nanum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Address;
import server.nanum.domain.Delivery;
import server.nanum.domain.User;
import server.nanum.domain.UserRole;
import server.nanum.dto.user.response.GuestDTO;
import server.nanum.dto.user.response.LoginResponseDTO;
import server.nanum.dto.user.response.UserDTO;
import server.nanum.repository.DeliveryRepository;
import server.nanum.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SignupService {
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;

    public void registerGuestUser(GuestDTO guestDTO) {
        User newUser = createUser(guestDTO);
        createDelivery(newUser, guestDTO);;
    }

    private User createUser(GuestDTO guestDTO) {
        User newUser = User.builder()
                .uid(0L) // "0000000000"으로 초기화
                .name(guestDTO.nickname())
                .userRole(UserRole.HOST)
                .inviteCode(UUID.randomUUID().toString())
                .build();

        return userRepository.save(newUser);
    }

    private void createDelivery(User user, GuestDTO guestDTO) {
        Address address = new Address(guestDTO.zipCode(), guestDTO.defaultAddress(), guestDTO.detailAddress());
        Delivery newDelivery = Delivery.builder()
                .nickname(guestDTO.nickname())
                .phoneNumber(null)
                .address(address)
                .isDefault(true)
                .user(user)
                .build();

        deliveryRepository.save(newDelivery);
    }
}

