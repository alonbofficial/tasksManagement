package com.lancellot.tasks.service.transformer;

import com.lancellot.tasks.repository.TaskRepository;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;

@AllArgsConstructor
public class BaseTransformerTask {

    protected final S3Client s3Client;
    private final String s3BucketName;
    protected final TaskRepository taskRepository;

    protected byte[] download(String key) {
        return s3Client.getObjectAsBytes(
                b -> b.bucket(s3BucketName).key(key)
        ).asByteArray();
    }
}
