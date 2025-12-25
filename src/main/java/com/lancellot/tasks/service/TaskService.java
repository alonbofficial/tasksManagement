package com.lancellot.tasks.service;

import com.lancellot.tasks.api.dto.TaskItemDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    public void transformTasks(@Valid TaskItemDto taskItemDto) {
        // Implementation of task transformation logic goes here

    }
}
