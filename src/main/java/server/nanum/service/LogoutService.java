package server.nanum.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.nanum.exception.JwtAuthenticationException;
import server.nanum.utils.JwtProvider;

/**
 * 로그아웃 서비스 클래스
 * 사용자 로그아웃을 수행하며, 로그아웃 시 토큰을 블랙리스트에 추가합니다.
 *
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023년 8월 10일
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LogoutService {
    private final JwtProvider jwtProvider;

    /**
     * 사용자 로그아웃을 수행합니다.
     * 로그아웃 요청을 받으면 해당 사용자의 토큰을 블랙리스트에 추가합니다.
     *
     * @param request HTTP 요청 객체, 토큰을 추출하기 위해 사용됩니다.
     * @throws JwtAuthenticationException 로그아웃 시도 중 토큰이 없는 경우 발생
     */
    public void logout(HttpServletRequest request) {
        String token = jwtProvider.extractTokenFromHeader(request)
                .orElseThrow(() -> new JwtAuthenticationException("로그아웃을 시도했으나 토큰이 없습니다!"));

        jwtProvider.addToBlacklist(token);
    }
}
