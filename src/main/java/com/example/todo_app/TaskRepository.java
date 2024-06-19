package com.example.todo_app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskDto, UUID> {

}
