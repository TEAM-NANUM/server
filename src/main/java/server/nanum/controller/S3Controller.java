package server.nanum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.nanum.dto.delivery.DeliveryListResponse;
import server.nanum.dto.s3.ImageListRequest;
import server.nanum.dto.s3.UrlListResponse;
import server.nanum.service.S3Service;

import java.net.URL;
import java.util.List;


/**
 * S3Controller
 * Amazon S3와의 통신을 위한 컨트롤러입니다.
 * Presigned URL의 생성을 담당하며, 해당 기능을 위한 서비스를 호출합니다.
 *
 * @author hyunjin
 * @version 1.0.0
 * @date 2023-08-12
 */
@Tag(name = "이미지 업로드 URL(Presigned URL) 제공 API", description = "이미지 저장 관련 API입니다. Object Stoage에 대한 Presigned URL 및 image_url을 제공합니다.")
@RestController
@RequestMapping("/api/presigned")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    /**
     * Presigned URL 리스트를 생성합니다.
     * 주어진 이미지 정보 목록에 대하여 Presigned URL을 생성하여 반환합니다.
     *
     * @param request 이미지 파일 이름과 타입을 담은 ImageListRequest 객체
     * @return Presigned URL의 리스트
     */
    @Operation(summary = "Presigned URL 생성 API",
            description = "ImageListRequest 객체를 입력받아 Presigned URL을 생성하는 API입니다.<br>" +
                    "응답 바디에서 presigned_url은 이미지 업로드 요청을 보낼 url, image_url은 이미지 주소입니다. 나중에 image_url을 넘겨줄 때 같이 넘겨주시면 됩니다.<br>"+
                    "test용 file_type : image/jpeg<br>"+
            " 자세한 사용법은 아래 링크를 참고해주세요<br>"+
            "https://www.notion.so/S3-chatGPT-dcf55112b6fd49708ecb191f85eaa321")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL 생성 성공", content = @Content(schema = @Schema(implementation = UrlListResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    public UrlListResponse getPresignedUrls(@Valid @RequestBody ImageListRequest request) {
        return s3Service.createPresignedUrls(request);
    }
}
