package com.lancellot.tasks.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class TaskDocumentDto {

    String fileName; //link_to_s3_object
    ArrayList<TaskItemDto> taskItems;
}
