package server.nanum.dto.s3;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import server.nanum.annotation.ImageFileType;

import java.util.List;

public record ImageListRequest(@Valid @JsonProperty("image_list") List<ImageInfo> imageList) {
    public record ImageInfo(
            @JsonProperty("file_name")
            @NotBlank(message = "파일명을 입력하셔야 됩니다!")
            String fileName,
            @NotBlank(message = "이미지 타입을 입력하셔야 됩니다!")
            @JsonProperty("file_type")
            @ImageFileType String fileType) {}
}
