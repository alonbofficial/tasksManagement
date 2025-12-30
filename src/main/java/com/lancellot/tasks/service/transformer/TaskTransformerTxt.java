package com.lancellot.tasks.service.transformer;

import com.lancellot.tasks.api.enums.FileType;
import com.lancellot.tasks.config.AwsProps;
import com.lancellot.tasks.domain.TaskItem;
import com.lancellot.tasks.repository.TaskRepository;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class TaskTransformerTxt extends TaskTransformerBase implements ITaskTransformer {

    public TaskTransformerTxt(S3Client s3Client, AwsProps awsProps, TaskRepository taskRepository) {
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

        List<TaskItem> taskItems = parseTasksFromText(content);
        saveTaskDocument(linkToFile, taskItems);
    }
}
