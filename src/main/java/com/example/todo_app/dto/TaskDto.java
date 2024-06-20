package com.example.todo_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.UUID;

/**
 * Data transfer object used to move data between the db and the controller.
 */
@Schema
@Entity
@Table(name = "tasks", schema = "mydatabase")
public class TaskDto implements Serializable {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, name = "description")
    public String description;

    @Id
    public UUID id;

    @Schema(defaultValue = "false", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(nullable = false, name = "completed")
    public boolean completed;

    public TaskDto(String description, UUID id, boolean completed) {
        this.description = description;
        this.id = id;
        this.completed = completed;
    }

    public TaskDto() {

    }

    public String getDescription() {
        return description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return completed;
    }
}
