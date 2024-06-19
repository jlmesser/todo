package com.example.todo_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    //todo handle 11 max incomplete

    private final TaskRepository taskRepository;


    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDto> getTasks() {
        return taskRepository.findAll();
    }

    public Optional<TaskDto> getTask(UUID id) {
        return taskRepository.findById(id);
    }

    public TaskDto addTask(TaskDto taskDto) {
        taskDto.setId(UUID.randomUUID()); //todo in db generation

        return taskRepository.save(taskDto);
    }

    public TaskDto updateTask(TaskDto taskDto) {
        //todo check exists

        return taskRepository.save(taskDto);
    }

    public void deleteTask(UUID taskId) {
        taskRepository.deleteById(taskId);
    }


}
