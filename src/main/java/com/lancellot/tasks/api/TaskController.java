package com.lancellot.tasks.api;

import com.lancellot.tasks.api.dto.TaskItemDto;
import com.lancellot.tasks.api.dto.TaskResponse;
import com.lancellot.tasks.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/transform")
    public ResponseEntity<?> transformTasks(@RequestBody @Valid TaskItemDto taskItemDto) {

        taskService.transformTasks(taskItemDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<TaskResponse> getTasksFromFile(@PathVariable @NotNull String fileName) {

        // Option 1: return directly from the db using TaskRepository
        // Option 2: return from TaskService which fetches from the db using TaskRepository
        return null;
    }
}
