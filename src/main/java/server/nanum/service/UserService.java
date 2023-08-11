package server.nanum.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.User;
import server.nanum.dto.user.response.HostGetResponseDTO;
import server.nanum.repository.TokenBlacklistRepository;
import server.nanum.repository.UserRepository;
import server.nanum.utils.JwtProvider;


/**
 * 사용자 서비스
 * 사용자와 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 *@author hyunjin
 * @version 1.0.0
 * @since 2023-08-05
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    /**
     * 사용자 정보 조회
     *
     * @param user 현재 사용자
     * @return HostGetResponseDTO 사용자 정보 응답 DTO
     */
    @Transactional(readOnly = true)
    public HostGetResponseDTO getUserInfo(User user) {
        return HostGetResponseDTO.toDTO(user);
    }

    /**
     * 포인트 충전
     *
     * @param user 현재 사용자
     * @param pointToAdd 충전할 포인트 양
     */
    public void chargePoint(User user, int pointToAdd) {
        int updatedPoint = user.getUserGroupPoint() + pointToAdd;
        user.getUserGroup().updatePoint(updatedPoint);
        userRepository.save(user);
    }

    /**
     * 사용자 삭제
     *
     * @param user 삭제할 사용자
     */
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}