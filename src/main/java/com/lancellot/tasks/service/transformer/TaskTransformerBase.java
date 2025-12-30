package com.lancellot.tasks.service.transformer;

import com.lancellot.tasks.domain.TaskDocument;
import com.lancellot.tasks.domain.TaskItem;
import com.lancellot.tasks.repository.TaskRepository;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class TaskTransformerBase {

    protected final S3Client s3Client;
    private final String s3BucketName;
    protected final TaskRepository taskRepository;

    protected byte[] download(String key) {
        return s3Client.getObjectAsBytes(
                b -> b.bucket(s3BucketName).key(key)
        ).asByteArray();
    }

    protected List<TaskItem> parseTasksFromText(String text) {
        List<TaskItem> taskItems = new ArrayList<>();
        Pattern pattern = Pattern.compile("^\\s*(\\d+)[.)\\s]+(.+)$");

        String[] lines = text.split("\\r?\\n");

        for (String line : lines) {

            line = line.replace("\r", "").trim();

            if (line.isBlank()) continue;

            Matcher matcher = pattern.matcher(line);

            if (matcher.matches()) {
                TaskItem taskItem = new TaskItem();
                taskItem.setNumber(Integer.parseInt(matcher.group(1)));
                taskItem.setDescription(matcher.group(2));
                taskItems.add(taskItem);
            }
        }

        return taskItems;
    }

    protected void saveTaskDocument(String fileName, List<TaskItem> taskItems) {
        TaskDocument taskDocument = new TaskDocument();
        taskDocument.setFileName(fileName);
        taskDocument.setTaskItems(new ArrayList<>(taskItems));
        taskRepository.save(taskDocument);
    }
}
