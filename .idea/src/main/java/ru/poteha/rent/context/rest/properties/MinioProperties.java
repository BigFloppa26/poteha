package ru.poteha.rent.context.rest.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Data
@Configuration
@ConfigurationProperties("spring.minio")
public class MinioProperties {
    private URI baseUri;
    private String region;
    private String accessKey;
    private String secretKey;

    private String algorithm = "AWS4-HMAC-SHA256";
}
