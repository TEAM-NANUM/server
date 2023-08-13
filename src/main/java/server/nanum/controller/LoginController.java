package server.nanum.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@Tag(name = "로그인 관련 API", description = "로그인  관련 API입니다. Host 로그인(kakao 로그인), Guest 로그인, 판매자 로그인이 수행됩니다.")
@RequestMapping("/api/login")
@Slf4j
public class LoginController {
    @Value("${frontendUrl}")
    private String frontendUrl;
    private final KakaoClient kakaoClient;
    private final LoginService loginService;

    /**
     * 카카오 로그인 페이지로 리다이렉트합니다.
     *
     * @return ResponseEntity<Void> 리다이렉트 응답
     */
    @Operation(summary = "카카오 로그인 API", description = "카카오 로그인 API입니다. 스웨거나 포스트맨으로는 동작하지 않고 브라우저창을 통해 직접 주소에 접근하셔야 됩니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공!, localhost:3000?token=?을 통해서 토큰을 전달합니다.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
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
        log.info("엑세스 토큰 = {}", token);

        String redirectUrl = frontendUrl + "?token=" + token;
        response.sendRedirect(redirectUrl);
    }


    /**
     * 게스트 로그인을 처리하고 로그인을 수행합니다.
     *
     * @param guestLoginRequestDTO 게스트 로그인 요청 DTO
     * @return LoginResponseDTO 로그인 또는 회원 가입 결과를 담은 응답 DTO
     */
    @Operation(summary = "게스트 로그인 API", description = "게스트 로그인 API입니다. 호스트가 등록한 게스트들은 초대코드를 통해서 손쉽게 로그인할 수 있습니다! ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공! HTTP Body를 통해서 사용자 정보를 전달합니다.", content = @Content(mediaType = "application/json" , schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/guest")
    public LoginResponseDTO loginGuest(@Valid @RequestBody GuestLoginRequestDTO guestLoginRequestDTO) {
        return loginService.loginOrCreate(guestLoginRequestDTO);
    }

    /**
     * 판매자 로그인을 처리하고 로그인을 수행합니다.
     *
     * @param sellerLoginRequestDTO 판매자 로그인 요청 DTO
     * @return LoginResponseDTO 로그인 또는 회원 가입 결과를 담은 응답 DTO
     */
    @Operation(summary = "판매자 로그인 API", description = "판매자 로그인 API입니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공! HTTP Body를 통해서 사용자 정보를 전달합니다.", content = @Content(mediaType = "application/json" ,schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "500", description= " 다뤄지지 않은 Server 오류, 백엔드 담당자에게 문의!", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/seller")
    public LoginResponseDTO loginGuest(@Valid @RequestBody SellerLoginRequestDTO sellerLoginRequestDTO) {
        return loginService.loginOrCreate(sellerLoginRequestDTO);
    }
}
