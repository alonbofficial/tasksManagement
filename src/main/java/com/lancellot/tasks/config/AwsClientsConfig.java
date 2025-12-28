package com.lancellot.tasks.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@AllArgsConstructor
public class AwsClientsConfig {

    private final Environment environment;

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        String accessKey = environment.getProperty("AWS_ACCESS_KEY_ID");
        String secretKey = environment.getProperty("AWS_SECRET_ACCESS_KEY");
        if (accessKey == null || secretKey == null) {
            throw new IllegalStateException("AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY must be set in environment");
        }
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
    }

    @Bean
    public Region awsRegion() {
        String region = environment.getProperty("AWS_REGION", "us-east-1");
        return Region.of(region);
    }

    @Bean
    public S3Client s3Client(AwsCredentialsProvider credentialsProvider, Region awsRegion) {
        return S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(awsRegion)
                .build();
    }
}
