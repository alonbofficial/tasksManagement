package com.lancellot.tasks.api;

import com.lancellot.tasks.api.dto.TaskDocumentDto;
import com.lancellot.tasks.api.dto.TaskItemDto;
import com.lancellot.tasks.api.dto.TaskResponse;
import com.lancellot.tasks.api.dto.TransformRequest;
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
    public ResponseEntity<?> transformTasks(@RequestBody @Valid TransformRequest transformRequest) {

        taskService.transformTasks(transformRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<TaskDocumentDto> getTasksFromFile(@PathVariable @NotNull String fileName) {

        TaskDocumentDto taskDocumentDto = taskService.getTasksFromFile(fileName);
        //TaskResponse taskResponse = new TaskResponse();
        //taskResponse.setTaskDocumentDto(taskDocumentDto);
        return ResponseEntity.ok(taskDocumentDto);
    }
}
