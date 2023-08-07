package server.nanum.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.nanum.dto.user.request.GuestLoginRequestDTO;
import server.nanum.dto.user.request.SellerLoginRequestDTO;
import server.nanum.dto.user.response.LoginResponseDTO;
import server.nanum.security.dto.KakaoAuthRequest;
import server.nanum.security.dto.KakaoUserResponse;
import server.nanum.security.oauth.KakaoClient;
import server.nanum.service.LoginService;

import java.io.IOException;

/**
 *회원 인증 컨트롤러
 * 판매자, 게스트, 호스트에 대한 로그인을 처리하는 컨트롤러입니다.
 *  카카오와 통신하여 인가 코드를 얻고, Access Token을 발급받아 사용자 정보를 가져오는 역할을 수행합니다.
 *서비스 레이어의 로그인 메서드를 호출하여 각 유형의 사용자 인증을 관리합니다.
 * Author: hyunjin
 * Version: 1.0.0
 * Date: 2023년 7월 30일
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
@Slf4j
public class LoginController {

    private final KakaoClient kakaoClient;
    private final LoginService loginService;

    /**
     * 카카오 로그인 페이지로 리다이렉트합니다.
     *
     * @return ResponseEntity<Void> 리다이렉트 응답
     */
    @GetMapping("/kakao")
    public ResponseEntity<Void> redirectToKakaoLogin() {
        return kakaoClient.redirectToKakaoAuth();
    }

    /**
     * 카카오 로그인 콜백을 처리하고 인증에 성공한 경우 로그인 또는 회원 가입을 수행합니다.
     *
     * @param authorizationCode 인가 코드
     * @return AuthResponseDTO 로그인 또는 회원 가입 결과를 담은 응답 DTO
     */
    @Hidden
    @GetMapping("/callback")
    public void processKakaoLoginCallback(@RequestParam("code") String authorizationCode, HttpServletResponse response) throws IOException {
        KakaoAuthRequest params = new KakaoAuthRequest(authorizationCode);
        KakaoUserResponse kakaoResponse = kakaoClient.handleCallback(params);
        LoginResponseDTO loginResponseDTO = loginService.loginOrCreate(kakaoResponse.toHostDTO());

        String token = loginResponseDTO.token();

        String redirectUrl = "http://localhost:3000/?token=" + token;
        response.sendRedirect(redirectUrl);
    }


    /**
     * 게스트 로그인을 처리하고 로그인을 수행합니다.
     *
     * @param guestLoginRequestDTO 게스트 로그인 요청 DTO
     * @return LoginResponseDTO 로그인 또는 회원 가입 결과를 담은 응답 DTO
     */
    @PostMapping("/guest")
    public LoginResponseDTO loginGuest(@RequestBody GuestLoginRequestDTO guestLoginRequestDTO) {
        return loginService.loginOrCreate(guestLoginRequestDTO);
    }

    /**
     * 판매자 로그인을 처리하고 로그인을 수행합니다.
     *
     * @param sellerLoginRequestDTO 판매자 로그인 요청 DTO
     * @return LoginResponseDTO 로그인 또는 회원 가입 결과를 담은 응답 DTO
     */
    @PostMapping("/seller")
    public LoginResponseDTO loginGuest(@RequestBody SellerLoginRequestDTO sellerLoginRequestDTO) {
        return loginService.loginOrCreate(sellerLoginRequestDTO);
    }
}
