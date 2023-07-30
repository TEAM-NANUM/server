package server.nanum.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserDTO implements UserDTO {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @JsonProperty("id")
    private Long uid;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private KakaoProfile profile;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        @JsonProperty("nickname")
        private String name;
    }

    public Long getUid() {
        return uid;
    }

    public String getName() {
        return kakaoAccount.getProfile().getName();
    }
}
