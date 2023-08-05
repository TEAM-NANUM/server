package server.nanum.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import server.nanum.config.properties.JwtProperties;
import server.nanum.domain.UserRole;
import server.nanum.exception.JwtAuthenticationException;

import javax.crypto.spec.SecretKeySpec;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * JWT 관리 클래스
 * JWT 토큰의 생성과 검증을 담당하는 클래스입니다.
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

    public String validateTokenAndGetSubject(String token) {
        try {
            String secretKey = jwtProperties.getSecretKey();
            return Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (SignatureException e) {
            throw new JwtAuthenticationException("시그니처가 일치하지 않습니다!");
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException("토큰이 만료되었습니다!");
        } catch (MalformedJwtException e) {
            throw new JwtAuthenticationException("잘못된 형식의 토큰입니다!");
        } catch (UnsupportedJwtException e) {
            throw new JwtAuthenticationException("지원하지 않는 토큰입니다!");
        }  catch (JwtException e) {
            throw new JwtAuthenticationException("요청 형식은 맞았으나 올바튼 토큰이 아닙니다!\n" + e.getMessage());
        }
    }
}
