package com.lancellot.tasks.api.dto;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record TaskDocumentDto (

    String fileName, //link_to_s3_object
    List<TaskItemDto> taskItems
){}
