package server.nanum.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import server.nanum.exception.JwtAuthenticationException;
import server.nanum.security.custom.CustomUserDetailsService;
import server.nanum.service.LogoutService;
import server.nanum.utils.JwtProvider;

import java.io.IOException;
import java.util.Optional;

import static server.nanum.utils.JWTErrorResponseWriter.*;

/**
 * JWT 인증 필터
 * HTTP 요청에서 JWT 토큰을 추출하여 사용자 인증을 수행하는 필터입니다.
 *
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023년 7월 30일
 */
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * HTTP 요청에서 JWT 토큰을 추출하여 사용자 인증을 수행합니다.
     * 토큰이 블랙리스트에 있는지 확인하고, 유효한 토큰인 경우 사용자 인증을 수행합니다.
     *
     * @param request     HTTP 요청 객체
     * @param response    HTTP 응답 객체
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException      입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            authenticateRequest(request);
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException e) {
            write(response, e.getMessage());
        }
    }

    /**
     * 요청에서 토큰을 추출하고, 토큰이 블랙리스트에 있는지 확인 후 인증을 수행합니다.
     */
    private void authenticateRequest(HttpServletRequest request) {
        Optional<String> tokenOptional = jwtProvider.extractTokenFromHeader(request);

        tokenOptional.ifPresent(token -> {
            if (jwtProvider.isTokenBlacklisted(token)) {
                throw new JwtAuthenticationException("이 토큰은 로그아웃 되어 현재 사용하실 수 없습니다!");
            }
            Authentication authentication = authenticateWithToken(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });
    }

    /**
     * 토큰을 이용하여 사용자 인증을 수행합니다.
     * 토큰의 subject 길이를 확인하고, 토큰을 검증하여 인증 객체를 반환합니다.
     *
     * @param token JWT 토큰 문자열
     * @return 인증 객체
     * @throws JwtAuthenticationException JWT 인증 실패 예외
     */
    private Authentication authenticateWithToken(String token) {
        String userInfo = Optional.ofNullable(token)
                .filter(subject -> subject.length() >= 10)
                .map(jwtProvider::validateTokenAndGetSubject)
                .orElseThrow(() -> new JwtAuthenticationException("페이로드가 올바르지 않습니다"));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userInfo);
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }
}
