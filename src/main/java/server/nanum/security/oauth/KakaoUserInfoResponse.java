package server.nanum.security.oauth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * KakaoUserInfoResponse
 * 카카오 사용자 정보 응답 클래스 (Json 형태 적용)
 * 카카오 서버로부터 사용자 정보를 받아올 때 이 클래스를 사용
 * 사용자의 닉네임 정보를 제공
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023-07-30
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponse {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private KakaoProfile profile;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        private String nickname;
    }

    public String getNickname() {
        return kakaoAccount.getProfile().getNickname();
    }
}
