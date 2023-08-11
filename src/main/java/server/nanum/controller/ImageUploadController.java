package server.nanum.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import server.nanum.utils.S3Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/images")
public class ImageUploadController {

    private final AmazonS3Client amazonS3Client;
    private final S3Util s3Util;

    public ImageUploadController(AmazonS3Client amazonS3Client, S3Util s3Util) {
        this.amazonS3Client = amazonS3Client;
        this.s3Util = s3Util;
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        File file = convertMultipartFileToFile(multipartFile);
        String fileName = multipartFile.getOriginalFilename();
        PutObjectRequest putRequest = new PutObjectRequest(s3Util.getBucket(), fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3Client.putObject(putRequest);
        file.delete(); // 임시 파일 삭제

        return s3Util.createImageUrl(fileName); // 파일 이름을 사용하여 URL 생성
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }
}
