package com.lancellot.tasks.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
public record AwsProps(S3 s3, String region) {
    public record S3(String bucket) {}
}
