package com.lancellot.tasks.service;

import com.lancellot.tasks.api.dto.TaskDocumentDto;
import com.lancellot.tasks.api.dto.TransformRequest;
import com.lancellot.tasks.domain.TaskDocument;
import com.lancellot.tasks.repository.TaskRepository;
import com.lancellot.tasks.service.transformer.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskTransformerRegistry taskTransformerRegistry;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public void transformTasks(@Valid TransformRequest taskItemDto) {

        TaskTransformer taskTransformer = taskTransformerRegistry.getMapValByFileType(taskItemDto.fileType());
        taskTransformer.transform(taskItemDto.linkToS3());
    }

    public TaskDocumentDto getTasksFromFile(@NotNull String fileName) {
        TaskDocument taskDocument = taskRepository.findByFileName(fileName);
        return modelMapper.map(taskDocument, TaskDocumentDto.class);
    }
}
