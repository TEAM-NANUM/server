package server.nanum.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.nanum.config.properties.S3Properties;

@Configuration
@RequiredArgsConstructor
public class S3Config {
    private final S3Properties s3Properties;
    private static final Logger logger = LoggerFactory.getLogger(S3Config.class);

    @Bean
    public AmazonS3Client amazonS3Client() {
        // 로깅 코드 추가
        logger.info("Access Key: {}", s3Properties.getAccessKey());
        logger.info("Secret Key: {}", s3Properties.getSecretKey());
        logger.info("Region: {}", s3Properties.getRegion());
        logger.info("Endpoint: {}", s3Properties.getEndpoint());
        logger.info("Bucket: {}", s3Properties.getBucket());
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(s3Properties.getAccessKey(), s3Properties.getSecretKey());
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3Properties.getEndpoint(), s3Properties.getRegion()))
                .build();
    }
}
