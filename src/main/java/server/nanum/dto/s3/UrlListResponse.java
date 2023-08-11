package server.nanum.dto.s3;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;
import java.util.List;

public record UrlListResponse(@JsonProperty("URLlist") List<UrlResponse> urlList) {
    public record UrlResponse(@JsonProperty("presigned_url") URL presignedUrl,
                                @JsonProperty("image_url") String imageUrl) {}
}
