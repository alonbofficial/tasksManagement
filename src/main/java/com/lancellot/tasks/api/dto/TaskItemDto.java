package com.lancellot.tasks.api.dto;

import com.lancellot.tasks.api.enums.FileType;

public record TaskItemDto(
    String linkToS3,
    FileType fileType
) {}
