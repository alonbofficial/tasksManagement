package com.lancellot.tasks.api.dto;

import lombok.Builder;

@Builder
public record TaskItemDto (
    Integer number,//mumeric_value_by_order
    String description //task_description
){}
