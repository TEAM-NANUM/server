package server.nanum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.nanum.domain.dto.user.AuthResponseDTO;
import server.nanum.security.dto.KakaoAuthRequest;
import server.nanum.domain.dto.user.KakaoUserDTO;
import server.nanum.security.oauth.KakaoClient;
import server.nanum.service.user.UserService;

/**
 * 회원 인증 컨트롤러
 * 카카오 OAuth2 인증 흐름을 처리합니다.
 * Kakao 서버와 통신하여 인가 코드를 얻고, Access Token을 발급받아 사용자 정보를 가져오는 역할을 수행합니다.
 *서비스 레이어의 로그인, 회원가입 메서드르 호출합니다.
 * Author: hyunjin
 * Version: 1.0.0
 * Date: 2023년 7월 30일
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
@Slf4j
public class AuthController {

    private final KakaoClient kakaoClient;
    private final UserService userService;

    /**
     * 카카오 로그인 페이지로 리다이렉트합니다.
     *
     * @return ResponseEntity<Void> 리다이렉트 응답
     */
    @GetMapping("/login")
    public ResponseEntity<Void> redirectToKakaoLogin() {
        return kakaoClient.redirectToKakaoAuth();
    }

    /**
     * 카카오 로그인 콜백을 처리하고 인증에 성공한 경우 로그인 또는 회원 가입을 수행합니다.
     *
     * @param authorizationCode 인가 코드
     * @return AuthResponseDTO 로그인 또는 회원 가입 결과를 담은 응답 DTO
     */
    @GetMapping("/callback")
    public AuthResponseDTO processKakaoLoginCallback(@RequestParam("code") String authorizationCode) {
        KakaoAuthRequest params = new KakaoAuthRequest(authorizationCode);
        KakaoUserDTO response = kakaoClient.handleCallback(params);

        log.info("사용자 이름: {}", response.getName());
        log.info("사용자 UID: {}", response.getUid());

        return userService.loginOrCreate(response);
    }
}
