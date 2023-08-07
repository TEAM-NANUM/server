package server.nanum.security.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import server.nanum.security.dto.KakaoAuthRequest;
import server.nanum.security.dto.KakaoUserResponse;

import java.net.URI;


/**
 * KakaoClient
 * 카카오와의 통신을 담당하는 클라이언트 클래스입니다.
 * KakaoAuthClient를 사용하여 액세스 토큰을 요청하고 사용자 정보를 가져옵니다.
 * KakaoAuthUrlBuilder를 사용하여 카카오 인증 URL을 생성합니다.
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023-07-30
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoClient {

    private final KakaoAuthClient authClient;
    private final KakaoAuthUrlBuilder authUrlBuilder;

    /**
     * 카카오 인증 URL로 리다이렉트하는 메서드입니다.
     * @return 리다이렉트 응답
     */
    public ResponseEntity<Void> redirectToKakaoAuth() {
        String authUrl = authUrlBuilder.buildKakaoAuthUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(authUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /**
     * 카카오 콜백으로부터 받은 인증 코드를 사용하여 로그인 처리를 합니다.
     * @param req KakaoAuthRequest 객체로부터 인증 정보를 받아옵니다.
     * @return 로그인 완료 메시지
     */
    public KakaoUserResponse handleCallback(KakaoAuthRequest req) {
        String accessToken = authClient.requestKakaoAccessToken(req);

        return authClient.requestKakaoUserInfo(accessToken);
    }
}
