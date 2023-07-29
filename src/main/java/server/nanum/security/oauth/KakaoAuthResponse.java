package server.nanum.security.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * KakaoAuthResponse
 * 카카오 API 인증 응답 클래스
 * Access Token을 포함한 응답 정보를 담고 있다.
 *
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023-07-29
 */
@Getter
@NoArgsConstructor
public class KakaoAuthResponse  {

    @JsonProperty("access_token")
    private String accessToken;

//    @JsonProperty("token_type")
//    private String tokenType;
//
//    @JsonProperty("refresh_token")
//    private String refreshToken;
//
//    @JsonProperty("expires_in")
//    private String expiresIn;
//
//    @JsonProperty("refresh_token_expires_in")
//    private String refreshTokenExpiresIn;
//
//    @JsonProperty("scope")
//    private String scope;
}
