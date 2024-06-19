package com.example.todo_app;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final HashMap<UUID, TaskDto> tasks = new HashMap<>();

    public List<TaskDto> getTasks() {
        return tasks.values().stream().toList();
    }

    public TaskDto getTask(UUID id) {
        return tasks.get(id);
    }

    public TaskDto addTask(TaskDto taskDto) {
        taskDto.setId(UUID.randomUUID());
        tasks.put(taskDto.getId(), taskDto);
        return taskDto;
    }

    public TaskDto updateTask(TaskDto taskDto) {
        tasks.put(taskDto.getId(), taskDto);
        return taskDto;
    }

    public TaskDto deleteTask(UUID taskId) {
        return tasks.remove(taskId);
    }



}
