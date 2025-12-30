package com.lancellot.tasks.service.transformer;

import com.lancellot.tasks.api.enums.FileType;
import com.lancellot.tasks.config.AwsProps;
import com.lancellot.tasks.domain.TaskDocument;
import com.lancellot.tasks.domain.TaskItem;
import com.lancellot.tasks.repository.TaskRepository;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TxtTaskTransformer extends BaseTransformerTask implements TaskTransformer {

    public TxtTaskTransformer(S3Client s3Client, AwsProps awsProps, TaskRepository taskRepository) {
        super(s3Client, awsProps.s3().bucket(), taskRepository);
    }

    @Override
    public FileType supports() {
        return FileType.TXT;
    }

    @Override
    public void transform(String linkToFile) {

        byte[] data = download(linkToFile);
        String content = new String(data, StandardCharsets.UTF_8);

        List<String> lines = content.lines().toList();

        TaskDocument taskDocument = new TaskDocument();
        taskDocument.setFileName(linkToFile);

        ArrayList<TaskItem> taskItemsList = new ArrayList<>();

        Pattern pattern = Pattern.compile("^\\s*(\\d+)[.)]\\s*(.+)$");

        for (String line : lines) {

            Matcher matcher = pattern.matcher(line);

            if (matcher.matches()) {
                if (line.isBlank()) continue;
                TaskItem taskItem = new TaskItem();
                taskItem.setNumber(Integer.parseInt(matcher.group(1)));
                taskItem.setDescription(matcher.group(2));
                taskItemsList.add(taskItem);
            }
        }

        taskDocument.setTaskItems(taskItemsList);
        taskRepository.save(taskDocument);
    }
}
