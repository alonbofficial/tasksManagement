package com.lancellot.tasks.repository;

import com.lancellot.tasks.domain.TaskDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TaskRepository extends MongoRepository<TaskDocument, String>{

    Optional<TaskDocument> findFirstByFileName(String fileName);
}
