package com.lancellot.tasks.repository;

import com.lancellot.tasks.domain.TaskDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<TaskDocument, String>{

    TaskDocument findByFileName(String fileName);
}
