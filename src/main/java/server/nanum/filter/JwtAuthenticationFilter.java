package server.nanum.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import server.nanum.dto.ErrorDTO;
import server.nanum.exception.JwtAuthenticationException;
import server.nanum.security.custom.CustomUserDetailsService;
import server.nanum.security.jwt.JwtProvider;
import server.nanum.utils.JWTErrorResponseWriter;

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
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException      입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> tokenOptional = extractTokenFromHeader(request);
            
            tokenOptional.ifPresent(token -> {
                Authentication authentication = authenticateWithToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            });

            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException e) {
            write(response, e.getMessage());
        }
    }

    /**
     * 요청 헤더에서 Bearer 토큰을 추출합니다.
     *
     * @param request HTTP 요청 객체
     * @return 추출한 토큰 문자열 (없을 경우 Optional.empty() 반환)
     */
    private Optional<String> extractTokenFromHeader(HttpServletRequest request) {
        Optional<String> authorizationHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));

        if (authorizationHeader.isEmpty()) {
            return Optional.empty();
        }

        if(!authorizationHeader.get().startsWith("Bearer ")){
            throw new JwtAuthenticationException("토큰 값 앞에 Bearer가 있어야 합니다!");
        }

        return Optional.of(authorizationHeader.get().substring(7));
    }

    /**
     * 토큰을 이용하여 사용자 인증을 수행합니다.
     *
     * @param token JWT 토큰 문자열
     * @return 인증 객체
     * @throws JwtAuthenticationException JWT 인증 실패 예외
     */
    private Authentication authenticateWithToken(String token) {
        String username = Optional.ofNullable(token)
                .filter(subject -> subject.length() >= 10)
                .map(jwtProvider::validateTokenAndGetSubject)
                .orElse("anonymous:anonymous")
                .split(":")[0];

        if (username.isEmpty()) {
            throw new JwtAuthenticationException("페이로드가 올바르지 않습니다");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }
}
