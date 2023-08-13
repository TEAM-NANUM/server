package server.nanum.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
@JsonPropertyOrder({"host","members"})
public record UserGroupListResponseDTO(
        @Schema(description = "호스트 정보")
        HostDTO host,
        @Schema(description = "그룹원 정보")
        List<GuestDTO> members) {
    @JsonPropertyOrder({"username","point"})
    public record HostDTO(
            @Schema(example = "나눔이",description = "사용자명")
            String username,

            @Schema(example = "1000",description = "포인트")
            int point) {
    }
    @JsonPropertyOrder({"nickname","inviteCode","defaultAddress"})
    public record GuestDTO(
            @Schema(example = "나눔게스트",description = "게스트명")
            String nickname,

            @Schema(example = "서울시 강남구",description = "주소")
            @JsonProperty("default_address")
            String defaultAddress,

            @Schema(example = "abcd1234",description = "우편번호")
            @JsonProperty("invite_code")
            String inviteCode) {
    }
}


