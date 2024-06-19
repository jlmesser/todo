package com.example.todo_app;

import java.util.UUID;

public class TaskDto {
    public String description;
    public UUID id;
    public boolean complete;

    public TaskDto(String description, UUID id, boolean complete) {
        this.description = description;
        this.id = id;
        this.complete = complete;
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

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
