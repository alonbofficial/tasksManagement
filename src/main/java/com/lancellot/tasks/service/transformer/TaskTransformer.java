package com.lancellot.tasks.service.transformer;

import com.lancellot.tasks.api.enums.FileType;

public interface TaskTransformer {

    FileType supports();
    void transform(String linkToS3);
}
