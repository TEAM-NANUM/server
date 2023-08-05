package server.nanum.service.adapter;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.User;
import server.nanum.domain.UserGroup;
import server.nanum.dto.user.request.HostLoginRequestDTO;
import server.nanum.dto.user.response.CommonLoginResponseDTO;
import server.nanum.repository.UserRepository;
import server.nanum.dto.user.request.UserLoginRequestDTO;
import server.nanum.security.jwt.JwtProvider;

import static server.nanum.dto.user.response.CommonLoginResponseDTO.*;

/**
 * 호스트 사용자 어댑터 클래스
 * 카카오 사용자 정보를 기반으로 로그인 또는 회원 가입을 처리하는 사용자 어댑터입니다.
 *
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023년 7월 30일
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HostUserAdapter implements UserAdapter {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final EntityManager entityManager;

    /**
     * 사용자 정보를 통해 해당 어댑터를 사용할 수 있는지 판단하는 메서드
     * @param userLoginRequestDTO 사용자 정보
     * @return 사용자 정보가 HostDTO의 인스턴스인 경우 true 반환
     */
    @Override
    public boolean supports(UserLoginRequestDTO userLoginRequestDTO) {
        return userLoginRequestDTO instanceof HostLoginRequestDTO;
    }

    /**
     * 로그인 처리를 하는 메서드
     * @param userLoginRequestDTO 사용자 정보
     * @return 로그인 응답 정보를 담은 LoginResponseDTO 반환
     */
    @Override
    public CommonLoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO) {
        HostLoginRequestDTO hostDTO = (HostLoginRequestDTO) userLoginRequestDTO;

        return userRepository.findByUid(hostDTO.uid())
                .map(this::createLoginResponse)
                .orElseGet(() -> {
                    UserGroup newUserGroup = UserGroup.createUserGroup(0); // 새로운 UserGroup 생성
                    entityManager.persist(newUserGroup);
                    User newUser = User.createHost(hostDTO, newUserGroup); // 생성자를 통해 UserGroup 설정
                    userRepository.save(newUser);
                    return createLoginResponse(newUser);
                });
    }

    /**
     * 로그인 응답 DTO를 생성하는 메서드
     * @param user 로그인한 사용자 정보
     * @return 로그인 응답 정보를 담은 LoginResponseDTO 반환
     */
    private CommonLoginResponseDTO createLoginResponse(User user) {
        String token = jwtProvider.createToken(String.format("%s:%s", user.getId(), user.getUserRole()));
        UserResponseDTO userResponseDTO = createUserResponseDTO(user);

        return CommonLoginResponseDTO.builder()
                .token(token)
                .userResponseDTO(userResponseDTO)
                .build();
    }

    /**
     * 사용자 응답 DTO를 생성하는 메서드
     * @param user 사용자 정보
     * @return 사용자 응답 정보를 담은 UserResponseDTO 반환
     */
    private UserResponseDTO createUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(String.valueOf((user.getId())))
                .username(user.getName())
                .role(String.valueOf(user.getUserRole()))
                .build();
    }
}
