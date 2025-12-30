package com.lancellot.tasks.service.transformer;

import com.lancellot.tasks.api.enums.FileType;
import com.lancellot.tasks.config.AwsProps;
import com.lancellot.tasks.domain.TaskItem;
import com.lancellot.tasks.repository.TaskRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaskTransformerCsv extends TaskTransformerBase implements ITaskTransformer {

    public TaskTransformerCsv(S3Client s3Client, AwsProps awsProps, TaskRepository taskRepository) {
        super(s3Client, awsProps.s3().bucket(), taskRepository);
    }

    @Override
    public FileType supports() {
        return FileType.CSV;
    }

    @Override
    public void transform(String linkToS3) {

        byte[] csvBytes = download(linkToS3);
        String csvContent = new String(csvBytes, StandardCharsets.UTF_8);

        List<TaskItem> taskItems = parseCsv(csvContent);
        saveTaskDocument(linkToS3, taskItems);
    }

    private List<TaskItem> parseCsv(String csvContent) {
        List<TaskItem> taskItems = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new StringReader(csvContent))) {
            String[] nextLine;
            boolean isFirstLine = true;

            while ((nextLine = reader.readNext()) != null) {
                // Skip header row (Number,Task)
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Parse: Column A = Number, Column B = Task
                if (nextLine.length >= 2) {
                    TaskItem taskItem = new TaskItem();
                    taskItem.setNumber(Integer.parseInt(nextLine[0].trim()));
                    taskItem.setDescription(nextLine[1].trim());
                    taskItems.add(taskItem);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return taskItems;
    }
}