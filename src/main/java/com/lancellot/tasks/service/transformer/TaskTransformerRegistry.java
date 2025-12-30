package com.lancellot.tasks.service.transformer;

import com.lancellot.tasks.api.enums.FileType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class TaskTransformerRegistry{

    private final Map<FileType, ITaskTransformer> mapFileTypeToTransformer;

    public TaskTransformerRegistry(List<ITaskTransformer> transformers) {

        EnumMap<FileType, ITaskTransformer> tmpFileTypeToTransformerMap = new EnumMap<>(FileType.class);

        for (ITaskTransformer t : transformers) {
            FileType type = t.supports();
            if (tmpFileTypeToTransformerMap.put(type, t) != null) {
                throw new IllegalStateException("Duplicate transformer for type: " + type);
            }
        }

        this.mapFileTypeToTransformer = Map.copyOf(tmpFileTypeToTransformerMap);
    }

    public ITaskTransformer getMapValByFileType(FileType type) {
        return mapFileTypeToTransformer.get(type);
    }
}

