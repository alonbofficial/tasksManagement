package com.lancellot.tasks.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.textract.TextractClient;

@Configuration
@RequiredArgsConstructor
public class AwsClientsConfig {

    private final AwsProps awsProps;

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");

        if (accessKey == null || secretKey == null) {
            throw new IllegalStateException("AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY must be set");
        }
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
    }

    @Bean
    public Region awsRegion() {
        return Region.of(awsProps.region() == null ? "us-east-1" : awsProps.region());
    }

    @Bean
    public S3Client s3Client(AwsCredentialsProvider credentialsProvider, Region awsRegion) {
        return S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(awsRegion)
                .build();
    }

    @Bean
    public TextractClient textractClient() {
        return TextractClient.builder()
                .region(Region.US_EAST_1) // or your region
                .build();
    }
}
