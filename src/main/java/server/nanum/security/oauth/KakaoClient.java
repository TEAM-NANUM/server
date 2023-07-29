package server.nanum.security.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import server.nanum.config.KakaoProperties;

/**
 * KakaoClient
 * 카카오 OAuth 서버와 통신하여 인증과 사용자 정보를 처리하는 클라이언트 클래스
 * 카카오 OAuth 인증을 통해 Access Token을 요청하고, Access Token을 이용하여 사용자 정보를 가져온다.
 *
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023-07-30
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoClient {

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;

    /**
     * 카카오 OAuth 인증을 통해 Access Token을 요청하는 메서드
     *
     * @param request 카카오 OAuth 인증 요청 정보를 담고 있는 객체
     * @return 카카오 OAuth 서버로부터 발급된 Access Token
     */
    public String requestAccessToken(KakaoAuthRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = request.makeBody();
        body.add("grant_type", kakaoProperties.getGrantType());
        body.add("client_id", kakaoProperties.getClientId());

        HttpEntity<?> httpEntity = new HttpEntity<>(body, httpHeaders);

        KakaoAuthResponse response = restTemplate.postForObject(kakaoProperties.getTokenUri(), httpEntity, KakaoAuthResponse.class);

        assert response != null;

        return response.getAccessToken();
    }

    /**
     * 카카오 서버로부터 사용자 정보를 요청하는 메서드
     *
     * @param accessToken 사용자 인증에 사용되는 Access Token
     * @return 카카오 서버로부터 받아온 사용자 정보를 담고 있는 객체
     */
    public KakaoUserInfoResponse requestUserInfo(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.profile\"]");

        HttpEntity<?> httpEntity = new HttpEntity<>(body, httpHeaders);

        return restTemplate.postForObject(kakaoProperties.getUserInfoUri(), httpEntity, KakaoUserInfoResponse.class);
    }
}
