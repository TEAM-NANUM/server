package server.nanum.service.adapter;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.User;
import server.nanum.domain.UserGroup;
import server.nanum.dto.user.request.HostLoginRequestDTO;
import server.nanum.dto.user.response.LoginResponseDTO;
import server.nanum.dto.user.response.LoginResponseFactory;
import server.nanum.repository.UserRepository;
import server.nanum.dto.user.request.UserLoginRequestDTO;
import server.nanum.security.jwt.JwtProvider;

/**
 * 호스트 사용자 어댑터 클래스
 * 카카오 사용자 정보를 기반으로 호스트에 대한 로그인 또는 회원 가입을 처리하는 사용자 어댑터입니다.
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
    private final EntityManager entityManager;
    private final JwtProvider jwtProvider;
    private final LoginResponseFactory loginResponseFactory;

    /**
     * 호스트 사용자 어댑터가 주어진 사용자 로그인 정보를 지원하는지 여부를 반환합니다.
     *
     * @param userLoginRequestDTO 사용자 로그인 정보
     * @return true(호스트 로그인 정보일 경우) / false(그 외의 경우)
     */
    @Override
    public boolean supports(UserLoginRequestDTO userLoginRequestDTO) {
        return userLoginRequestDTO instanceof HostLoginRequestDTO;
    }

    /**
     * 호스트 사용자를 로그인 또는 회원 가입 처리합니다.
     * 이미 등록된 호스트일 경우 로그인을 수행하고, 새로운 호스트일 경우 회원 가입을 수행합니다.
     *
     * @param userLoginRequestDTO 호스트 사용자의 로그인 또는 회원 가입 정보
     * @return 인증 응답 DTO
     */
    @Override
    public LoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO) {
        HostLoginRequestDTO hostDTO = (HostLoginRequestDTO) userLoginRequestDTO;

        return userRepository.findByUid(hostDTO.uid())
                .map(this::createLoginResponse)
                .orElseGet(() -> {
                    UserGroup newUserGroup = UserGroup.createUserGroup(0);
                    entityManager.persist(newUserGroup);
                    User newUser = User.createHost(hostDTO, newUserGroup);
                    userRepository.save(newUser);
                    return createLoginResponse(newUser);
                });
    }

    private LoginResponseDTO createLoginResponse(User user) {
        String token = jwtProvider.createToken(String.format("%s:%s", user.getId(), user.getUserRole()));
        return loginResponseFactory.createLoginResponseDTO(user, token);
    }
}
