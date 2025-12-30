package com.lancellot.tasks.service.transformer;

import com.lancellot.tasks.api.enums.FileType;
import com.lancellot.tasks.config.AwsProps;
import com.lancellot.tasks.domain.TaskDocument;
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

import java.util.ArrayList;
import java.util.List;

@Component
public class ImgTaskTransformer extends BaseTransformerTask implements TaskTransformer {

    private final TextractClient textractClient;
    private final AwsProps awsProps;

    public ImgTaskTransformer(S3Client s3Client, AwsProps awsProps, TaskRepository taskRepository, TextractClient textractClient) {
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

        try {
            // Extract text from image using Textract
            String extractedText = extractTextFromImage(linkToS3);

            // Parse the extracted text to get tasks
            List<TaskItem> taskItems = parseTasksFromText(extractedText);

            // Save to database
            TaskDocument taskDocument = new TaskDocument();
            taskDocument.setFileName(linkToS3);
            taskDocument.setTaskItems(new ArrayList<>(taskItems));

            taskRepository.save(taskDocument);

        } catch (Exception e) {
            throw new RuntimeException("Failed to process image file: " + linkToS3, e);
        }
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

    private List<TaskItem> parseTasksFromText(String extractedText) {

        List<TaskItem> taskItems = new ArrayList<>();
        String[] lines = extractedText.split("\n");

        for (String line : lines) {
            line = line.trim();

            // Parse lines like "1 Mow the lawn" or "1. Mow the lawn"
            if (line.matches("^\\d+[.\\s].*")) {
                String[] parts = line.split("[.\\s]+", 2);
                if (parts.length >= 2) {
                    try {
                        int number = Integer.parseInt(parts[0]);
                        String description = parts[1].trim();

                        TaskItem taskItem = new TaskItem();
                        taskItem.setNumber(number);
                        taskItem.setDescription(description);
                        taskItems.add(taskItem);
                    } catch (NumberFormatException e) {
                        // Skip lines that don't have valid numbers
                    }
                }
            }
        }

        return taskItems;
    }
}
