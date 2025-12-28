package com.lancellot.tasks.service.transformer;

import com.lancellot.tasks.api.enums.FileType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class TaskTransformerRegistry{

    private final Map<FileType, TaskTransformer> mapFileTypeToTransformer;

    public TaskTransformerRegistry(List<TaskTransformer> transformers) {

        EnumMap<FileType, TaskTransformer> tmpFileTypeToTransformerMap = new EnumMap<>(FileType.class);

        for (TaskTransformer t : transformers) {
            FileType type = t.supports();
            if (tmpFileTypeToTransformerMap.put(type, t) != null) {
                throw new IllegalStateException("Duplicate transformer for type: " + type);
            }
        }

        this.mapFileTypeToTransformer = Map.copyOf(tmpFileTypeToTransformerMap);
    }

    public TaskTransformer getMapValByFileType(FileType type) {
        return mapFileTypeToTransformer.get(type);
    }
}

