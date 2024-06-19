package com.example.todo_app;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tasks", schema = "mydatabase")
public class TaskDto implements Serializable {

    @Column(nullable = false, name = "description")
    public String description;

    @Id
    public UUID id;

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

    public void setDescription(String description) {
        this.description = description;
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

    public void setCompleted(boolean complete) {
        this.completed = complete;
    }
}
