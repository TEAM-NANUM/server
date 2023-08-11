package server.nanum.utils;

import org.springframework.stereotype.Component;
import server.nanum.config.properties.S3Properties;

@Component
public class S3Util {

    private final S3Properties s3Properties;

    public S3Util(S3Properties s3Properties) {
        this.s3Properties = s3Properties;
    }

    // 이미지 URL을 조합하는 메서드
    public String createImageUrl(String fileName) {
        return s3Properties.getEndpoint() + "/" + s3Properties.getBucket() + "/" + fileName;
    }

    public String getBucket() {
        return s3Properties.getBucket();
    }
}
