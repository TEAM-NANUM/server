package server.nanum.service.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.domain.User;
import server.nanum.dto.user.request.GuestLoginRequestDTO;
import server.nanum.dto.user.request.UserLoginRequestDTO;
import server.nanum.dto.user.response.LoginResponseDTO;
import server.nanum.dto.user.response.LoginResponseFactory;
import server.nanum.repository.UserRepository;
import server.nanum.security.jwt.JwtProvider;


/**
 * 게스트 사용자 어댑터 클래스
 * 호스트의 초대 코드를 기반으로 게스트에 대한 로그인을 처리하는 사용자 어댑터입니다.
 *
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023년 7월 30일
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GuestUserAdapter implements UserAdapter {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final LoginResponseFactory loginResponseFactory;

    /**
     * 게스트 사용자 어댑터가 주어진 사용자 로그인 정보를 지원하는지 여부를 반환합니다.
     *
     * @param userLoginRequestDTO 사용자 로그인 정보
     * @return true(게스트 로그인 정보일 경우) / false(그 외의 경우)
     */
    @Override
    public boolean supports(UserLoginRequestDTO userLoginRequestDTO) {
        return userLoginRequestDTO instanceof GuestLoginRequestDTO;
    }

    /**
     * 게스트 사용자를 로그인 처리합니다.
     * 주어진 초대 코드를 기반으로 호스트가 존재하는지 확인하고, 해당 게스트를 인증하여 로그인을 수행합니다.
     *
     * @param userLoginRequestDTO 게스트 사용자의 로그인 정보 (초대 코드 포함)
     * @return 인증 응답 DTO
     * @throws RuntimeException 호스트가 존재하지 않을 경우 예외를 던집니다.
     */
    @Override
    public LoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO) {
        GuestLoginRequestDTO guestDTO = (GuestLoginRequestDTO) userLoginRequestDTO;

        User guest = userRepository.findByInviteCode(guestDTO.inviteCode())
                .orElseThrow(() -> new RuntimeException("초대코드가 올바르지 않거나 존재하지 않습니다!"));

        return createLoginResponse(guest);
    }

    private LoginResponseDTO createLoginResponse(User user) {
        String token = jwtProvider.createToken(String.format("%s:%s", user.getId(), user.getUserRole()));
        return loginResponseFactory.createLoginResponseDTO(user, token);
    }
}


