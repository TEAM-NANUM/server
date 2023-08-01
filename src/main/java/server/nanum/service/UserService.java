package server.nanum.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.User;
import server.nanum.dto.user.HostResponseDTO;
import server.nanum.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public HostResponseDTO getUserInfo(User user) {

        // UserGroup을 즉시 초기화합니다.
        Hibernate.initialize(user.getUserGroup());

        // 서비스 계층에서 DTO를 생성
        log.info("UserGroup = {}", user.getUserGroup().getId());
        return HostResponseDTO.builder()
                .Id(String.valueOf(user.getId()))
                .name(user.getName())
                .isGuest(false)
                .point(user.getUserGroupPoint())
                .build();
    }
}