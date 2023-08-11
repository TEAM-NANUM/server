package server.nanum.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.nanum.dto.s3.ImageListRequest;
import server.nanum.dto.s3.UrlListResponse;
import server.nanum.utils.S3Util;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static server.nanum.dto.s3.UrlListResponse.*;

/**
 * S3Service
 * Amazon S3와의 통신을 담당하는 서비스 클래스입니다.
 * 여러 파일 또는 단일 파일에 대한 Presigned URL을 생성하며,
 * 생성된 Presigned URL과 함께 해당 파일의 이미지 URL도 제공합니다.
 * @author hyunjin
 * @version 1.0.0
 * @date 2023-08-12
 */
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;
    private final S3Util s3Util;

    /**
     * 여러 파일에 대한 presigned URL을 생성하는 메서드입니다.
     * ImageListRequest 객체를 받아 각 파일에 대한 Presigned URL과 이미지 URL을 생성합니다.
     *
     * @param request ImageListRequest 객체, 이미지 정보를 담고 있음
     * @return UrlListResponse 객체, Presigned URL과 이미지 URL 리스트
     */
    public UrlListResponse createPresignedUrls(ImageListRequest request) {
        List<UrlResponse> urlResponses = request.imageList()
                .stream()
                .map(imageInfo -> {
                    String fileName = createFileName(imageInfo.fileName());
                    URL presignedUrl = createPresignedUrl(fileName, imageInfo.fileType());
                    String imageUrl = s3Util.createImageUrl(fileName);
                    return new UrlResponse(presignedUrl, imageUrl);
                })
                .collect(Collectors.toList());

        return new UrlListResponse(urlResponses);
    }

    /**
     * 파일 이름을 생성하는 메서드입니다.
     * 현재 날짜와 시간, 원본 파일 이름을 조합하여 생성합니다.
     *
     * @param originalFileName 원본 파일 이름
     * @return 생성된 파일 이름
     */
    private String createFileName(String originalFileName) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + originalFileName;
    }

    /**
     * 한 파일에 대한 presigned URL을 생성하는 메서드입니다.
     * 파일 이름과 파일 타입을 받아 해당 파일의 Presigned URL을 생성합니다.
     *
     * @param fileName 생성된 파일 이름
     * @param fileType 파일의 MIME 타입
     * @return Presigned URL
     */
    private URL createPresignedUrl(String fileName, String fileType) {
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + 1000 * 60 * 60);
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(s3Util.getBucket(), fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());
        generatePresignedUrlRequest.setContentType(fileType);

        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }
}