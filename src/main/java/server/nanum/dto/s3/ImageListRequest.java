package server.nanum.dto.s3;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ImageListRequest(@JsonProperty("image_list") List<ImageInfo> imageList) {
    public record ImageInfo(
            @JsonProperty("file_name") String fileName,
            @JsonProperty("file_type") String fileType) {}
}
