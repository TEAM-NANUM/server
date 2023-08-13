package server.nanum.dto.s3;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.net.URL;
import java.util.List;

public record UrlListResponse(
        @JsonProperty("URLlist")
        @Schema(description = "Url 정보")
        List<UrlResponse> urlList) {
    public record UrlResponse(
            @JsonProperty("presigned_url")
            @Schema(description = "서명 Url")
            URL presignedUrl,
            @JsonProperty("image_url")
            @Schema(description = "이미지 Url")
            String imageUrl){}
}
