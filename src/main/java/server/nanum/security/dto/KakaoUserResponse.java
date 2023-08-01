package server.nanum.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import server.nanum.dto.user.HostDTO;
import server.nanum.dto.user.UserDTO;

/**
 * KakaoUserDTO
 * 카카오API 서버로부터 받은 응답객체
 * 사용자의 닉네임 정보를 제공
 * 작성자: hyunjin
 * 버전: 1.0.0
 * 작성일: 2023-07-30
 */
@Getter
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserResponse  {

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

    public HostDTO toHostDTO() {
        return HostDTO.builder()
                .uid(uid)
                .name(getName())
                .build();
    }
}
