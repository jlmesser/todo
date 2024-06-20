package com.example.todo_app.repository;

import com.example.todo_app.dto.TaskDto;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository used to communicate with Postgres via JPA
 */
@Hidden
@Repository
public interface TaskRepository extends JpaRepository<TaskDto, UUID> {

    List<TaskDto> findByCompletedFalse();

}
