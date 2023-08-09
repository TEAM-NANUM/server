package server.nanum.utils;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import server.nanum.config.properties.JwtProperties;
import server.nanum.domain.TokenBlacklist;
import server.nanum.domain.UserRole;
import server.nanum.exception.JwtAuthenticationException;
import server.nanum.repository.TokenBlacklistRepository;

import javax.crypto.spec.SecretKeySpec;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

/**
 * JWT 관리 클래스
 * JWT 토큰의 생성, 검증, 추출, 블랙리스트 관리 등을 담당하는 클래스입니다.
 * JwtProperties 객체를 이용하여 토큰을 설정하고 관리합니다.
 *
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023년 7월 30일
 */
@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    /**
     * 사용자 정보를 기반으로 JWT 토큰을 생성합니다.
     *
     * @param userSpecification 사용자 정보
     * @return 생성된 JWT 토큰
     */
    public String createToken(String userSpecification) {
        String secretKey = jwtProperties.getSecretKey();
        long expirationHours = jwtProperties.getExpirationHours();
        String issuer = jwtProperties.getIssuer();

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                .setSubject(userSpecification)
                .setIssuer(issuer)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)))
                .compact();
    }

    /**
     * HTTP 요청 헤더에서 JWT 토큰을 추출합니다.
     *
     * @param request HTTP 요청 객체
     * @return 추출된 JWT 토큰
     */
    public Optional<String> extractTokenFromHeader(HttpServletRequest request) {
        Optional<String> authorizationHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));

        if (authorizationHeader.isEmpty()) {
            return Optional.empty();
        }

        if (!authorizationHeader.get().startsWith("Bearer ")) {
            throw new JwtAuthenticationException("토큰 값 앞에 Bearer가 있어야 합니다!");
        }

        return Optional.of(authorizationHeader.get().substring(7));
    }

    /**
     * JWT 토큰을 검증하고, 사용자 정보를 반환합니다.
     *
     * @param token JWT 토큰 문자열
     * @return 사용자 정보
     * @throws JwtAuthenticationException JWT 인증 실패 예외
     */
    public String validateTokenAndGetSubject(String token) {
        try {
            return validateToken(token);
        } catch (JwtException e) {
            throw new JwtAuthenticationException(generateErrorMessage(e));
        }
    }

    private String validateToken(String token) {
        String secretKey = jwtProperties.getSecretKey();
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private String generateErrorMessage(JwtException e) {
        if (e instanceof SignatureException) return "시그니처가 일치하지 않습니다!";
        if (e instanceof ExpiredJwtException) return "토큰이 만료되었습니다!";
        if (e instanceof MalformedJwtException) return "잘못된 형식의 토큰입니다!";
        if (e instanceof UnsupportedJwtException) return "지원하지 않는 토큰입니다!";
        return "요청 형식은 맞았으나 올바른 토큰이 아닙니다!\n" + e.getMessage();
    }

    /**
     * 토큰을 블랙리스트에 추가합니다.
     *
     * @param token JWT 토큰 문자열
     */
    public void addToBlacklist(String token) {
        TokenBlacklist tokenBlacklist = new TokenBlacklist(token);
        tokenBlacklistRepository.save(tokenBlacklist);
    }

    /**
     * 토큰이 블랙리스트에 있는지 확인합니다.
     *
     * @param token JWT 토큰 문자열
     * @return 블랙리스트에 있으면 true, 그렇지 않으면 false
     */
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.findById(token).isPresent();
    }
}
