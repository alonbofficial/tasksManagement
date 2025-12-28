package com.lancellot.tasks.service;

import com.lancellot.tasks.api.dto.TaskItemDto;
import com.lancellot.tasks.service.transformer.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskTransformerRegistry taskTransformerRegistry;

    public void transformTasks(@Valid TaskItemDto taskItemDto) {

        TaskTransformer taskTransformer = taskTransformerRegistry.getMapValByFileType(taskItemDto.fileType());
        taskTransformer.transform(taskItemDto.linkToS3());
    }
}
