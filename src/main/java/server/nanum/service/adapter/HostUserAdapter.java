package server.nanum.service.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.Seller;
import server.nanum.domain.User;
import server.nanum.dto.user.HostDTO;
import server.nanum.dto.user.LoginResponseDTO;
import server.nanum.repository.UserRepository;
import server.nanum.security.dto.KakaoUserResponse;
import server.nanum.dto.user.UserDTO;
import server.nanum.security.jwt.JwtProvider;

import static server.nanum.domain.User.createHost;
import static server.nanum.dto.user.LoginResponseDTO.*;

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

    @Override
    public boolean supports(UserDTO userDTO) {
        return userDTO instanceof HostDTO;
    }

    @Override
    public LoginResponseDTO login(UserDTO userDTO) {
        HostDTO hostDTO = (HostDTO) userDTO;

        return userRepository.findByUid(hostDTO.uid())
                .map(user -> createLoginResponse(user))
                .orElseGet(() -> {
                    User newUser = createHost(hostDTO);
                    userRepository.save(newUser);
                    return createLoginResponse(newUser);
                });
    }

    private LoginResponseDTO createLoginResponse(User user) {
        String token = jwtProvider.createToken(String.format("%s:%s", user.getId(), user.getUserRole()));
        UserResponseDTO userResponseDTO = createUserResponseDTO(user);

        return LoginResponseDTO.builder()
                .token(token)
                .user(userResponseDTO)
                .build();
    }

    private UserResponseDTO createUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getName())
                .role("HOST")
                .build();
    }
}

