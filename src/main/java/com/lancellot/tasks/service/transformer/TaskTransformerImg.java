package com.lancellot.tasks.service.transformer;

import com.lancellot.tasks.api.enums.FileType;
import com.lancellot.tasks.config.AwsProps;
import com.lancellot.tasks.domain.TaskItem;
import com.lancellot.tasks.repository.TaskRepository;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextResponse;
import software.amazon.awssdk.services.textract.model.Document;
import software.amazon.awssdk.services.textract.model.S3Object;

import java.util.List;

@Component
public class TaskTransformerImg extends TaskTransformerBase implements ITaskTransformer {

    private final TextractClient textractClient;
    private final AwsProps awsProps;

    public TaskTransformerImg(S3Client s3Client, AwsProps awsProps, TaskRepository taskRepository, TextractClient textractClient) {
        super(s3Client, awsProps.s3().bucket(), taskRepository);
        this.textractClient = textractClient;
        this.awsProps = awsProps;
    }

    @Override
    public FileType supports() {
        return FileType.IMG;
    }

    @Override
    public void transform(String linkToS3) {
        String extractedText = extractTextFromImage(linkToS3);
        List<TaskItem> taskItems = parseTasksFromText(extractedText);
        saveTaskDocument(linkToS3, taskItems);
    }

    private String extractTextFromImage(String linkToS3) {

        DetectDocumentTextRequest request = DetectDocumentTextRequest.builder()
                .document(Document.builder()
                        .s3Object(S3Object.builder()
                                .bucket(awsProps.s3().bucket())
                                .name(linkToS3)
                                .build())
                        .build())
                .build();

        DetectDocumentTextResponse response = textractClient.detectDocumentText(request);

        // Extract text from all blocks
        StringBuilder fullText = new StringBuilder();
        for (Block block : response.blocks()) {
            if (block.blockType() == BlockType.LINE) {
                fullText.append(block.text()).append("\n");
            }
        }

        return fullText.toString();
    }
}
