package com.lancellot.tasks.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "task_documents")
public class TaskDocument {

    @Id
    @JsonIgnore
    String id; //object_id
    @Getter @Setter
    String fileName; //link_to_s3_object
    @Getter @Setter
    ArrayList<TaskItem> taskItems = new ArrayList<>();
}
