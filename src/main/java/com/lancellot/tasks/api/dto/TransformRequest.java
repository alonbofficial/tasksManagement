package com.lancellot.tasks.api.dto;

import com.lancellot.tasks.api.enums.FileType;

public record  TransformRequest(
    String linkToS3,
    FileType fileType
){}
