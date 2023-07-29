package server.nanum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.nanum.security.oauth.KakaoAuthRequest;
import server.nanum.security.oauth.KakaoClient;
import server.nanum.security.oauth.KakaoUserInfoResponse;
import server.nanum.config.KakaoProperties;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 카카오 회원 인증 컨트롤러
 * 카카오 OAuth2 인증 흐름을 처리합니다.
 * Kakao 서버와 통신하여 인가 코드를 얻고, Access Token을 발급받아 사용자 정보를 가져오는 역할을 수행합니다.
 *
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023-07-30
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class KakaoAuthController {

    private final KakaoClient kakaoClient;
    private final KakaoProperties kakaoProperties;

    private static final String RESPONSE_TYPE_CODE = "code";

    /**
     * 인가 코드를 받기 위해 카카오 서버 로그인 화면으로 리다이렉트해주는 메소드
     * @return 302 Redirect와 함께 카카오 인증 화면으로 리다이렉트되는 ResponseEntity
     */
    @GetMapping("/auth")
    public ResponseEntity<Void> redirectToKakaoAuth() {
        String kakaoAuthUrl = buildKakaoAuthUrl();
        log.info("kakaoAuthUrl = {}", kakaoAuthUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(kakaoAuthUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /**
     * Kakao 서버 콜백 엔드포인트로부터 인가 코드를 전달받는 메소드
     * 인가 코드를 기반으로 Access Token을 발급받고, 사용자 정보를 가져와 처리
     *
     * @param authorizationCode Kakao 서버로부터 전달받은 인가 코드
     * @return 인가 코드 처리 결과 메시지
     */
    @GetMapping("/callback")
    public String handleKakaoCallback(@RequestParam("code") String authorizationCode) {
        // KakaoRequest 객체 생성
        KakaoAuthRequest params = new KakaoAuthRequest(authorizationCode);

        // 인가 코드를 바탕으로 Access Token 발급을 위한 카카오 서버 요청 진행
        String accessToken = kakaoClient.requestAccessToken(params);
        log.info("kakao access token = {}", accessToken);

        // 얻은 Access Token을 활용하여 사용자 정보를 가져오는 카카오 서버 요청 진행
        KakaoUserInfoResponse kakaoUserInfoResponse = kakaoClient.requestUserInfo(accessToken);
        log.info("nickname = {}", kakaoUserInfoResponse.getNickname());

        // 사용자 정보를 활용하여 원하는 동작 수행
        // ...

        return "로그인 완료";
    }

    /**
     * 카카오 인증 URL을 구성하는 메서드입니다.
     *
     * @return 카카오 인증 URL
     */
    private String buildKakaoAuthUrl() {
        String authorizationUri = kakaoProperties.getAuthorizationUri();
        String clientId = encodeQueryParam(kakaoProperties.getClientId());
        String redirectUri = encodeQueryParam(kakaoProperties.getRedirectUri());

        return String.format(
                "%s?client_id=%s&redirect_uri=%s&response_type=%s",
                authorizationUri, clientId, redirectUri, RESPONSE_TYPE_CODE);
    }

    /**
     * 주어진 쿼리 파라미터를 UTF-8로 인코딩하는 메서드입니다.
     *
     * @param param 인코딩할 쿼리 파라미터
     * @return 인코딩된 쿼리 파라미터
     */
    private String encodeQueryParam(String param) {
        return URLEncoder.encode(param, StandardCharsets.UTF_8);
    }
}
