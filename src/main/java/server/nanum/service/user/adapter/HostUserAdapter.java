package server.nanum.service.user.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.User;
import server.nanum.domain.dto.user.AuthResponseDTO;
import server.nanum.repository.UserRepository;
import server.nanum.domain.dto.user.KakaoUserDTO;
import server.nanum.domain.dto.user.UserDTO;
import server.nanum.utils.JwtProvider;

import static server.nanum.domain.User.createHost;
import static server.nanum.domain.dto.user.AuthResponseDTO.*;


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

    /**
     * 주어진 사용자 정보 객체가 KakaoUserDTO인지 확인하여 지원 여부를 판별합니다.
     *
     * @param userDTO 사용자 정보 객체
     * @return KakaoUserDTO인 경우 true, 그렇지 않은 경우 false
     */
    @Override
    public boolean supports(UserDTO userDTO) {
        return userDTO instanceof KakaoUserDTO;
    }

    /**
     * Kakao 사용자 정보를 기반으로 로그인 또는 회원 가입을 처리합니다.
     * 이미 존재하는 사용자인 경우 로그인하고, 새로운 사용자인 경우 회원 가입을 수행합니다.
     *
     * @param userDTO Kakao 사용자 정보 객체
     * @return 인증 응답 DTO
     */
    @Override
    public AuthResponseDTO loginOrRegister(UserDTO userDTO) {
        KakaoUserDTO kakaoUserDTO = (KakaoUserDTO) userDTO;
        Long uid = kakaoUserDTO.getUid();

        return userRepository.findByUid(uid)
                .map(user -> {
                    log.info("이미 존재하는 사용자입니다. = {}", user.getName());
                    return login(user);
                })
                .orElseGet(() -> {
                    log.info("새로운 사용자입니다.");
                    return register(userDTO);
                });
    }

    /**
     * 새로운 사용자를 회원가입 처리하고, 로그인 처리를 수행합니다.
     *
     * @param userDTO Kakao 사용자 정보 객체
     * @return 인증 응답 DTO
     */
    private AuthResponseDTO register(UserDTO userDTO) {
        KakaoUserDTO kakaoUserDTO = (KakaoUserDTO) userDTO;
        // 회원가입 처리 로직 구현
        log.info("회원가입 처리 로직 호출");
        User newUser = createHost(kakaoUserDTO);
        userRepository.save(newUser);

        return login(newUser);
    }

    /**
     * 사용자를 로그인 처리하고 인증 응답 DTO를 생성합니다.
     *
     * @param user 로그인 대상 사용자
     * @return 인증 응답 DTO
     */
    private AuthResponseDTO login(User user) {
        String token = jwtProvider.createToken(String.valueOf(user.getId()));
        UserResponseDTO userResponseDTO = createUserResponseDTO(user);

        return AuthResponseDTO.builder()
                .token(token)
                .user(userResponseDTO)
                .build();
    }

    /**
     * 사용자 객체를 기반으로 사용자 응답 DTO를 생성합니다.
     *
     * @param user 사용자 객체
     * @return 사용자 응답 DTO
     */
    private UserResponseDTO createUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getName())
                .role("HOST")
                .build();
    }
}

