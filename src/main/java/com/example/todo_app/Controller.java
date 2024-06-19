package com.example.todo_app;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.rmi.ServerException;
import java.util.List;
import java.util.UUID;

@RestController
public class Controller {

    TaskService taskService;

    @Autowired
    public Controller(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getTasks() throws ServerException {

        List<TaskDto> tasks = taskService.getTasks();

        if (tasks != null) {
            return ResponseEntity.ok(tasks);
        } else {
            throw new ServerException("Error in creating the User resource. Try Again.");
        }
    }

    @GetMapping(value = "/task/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> getTask(@PathVariable UUID taskId) throws ServerException {
        TaskDto task = taskService.getTask(taskId);

        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            throw new ServerException("Error in creating the User resource. Try Again.");
        }
    }

    @PostMapping(path = "/task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto newTask, HttpServletRequest request) throws ServerException {

        TaskDto task = taskService.addTask(newTask);

        if (task != null) {
            URI location = ServletUriComponentsBuilder.fromRequestUri(request).path("/{id}").buildAndExpand(task.getId()).toUri();
            return ResponseEntity.created(location).body(task);
        } else {
            throw new ServerException("Error in creating the User resource. Try Again.");
        }
    }

    @PutMapping(value = "/task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto updatedTask) throws ServerException {

        TaskDto task = taskService.updateTask(updatedTask);

        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            throw new ServerException("Error in creating the User resource. Try Again.");
        }
    }

    @DeleteMapping(value = "/task/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> deleteTask(@PathVariable UUID taskId) throws ServerException {

        TaskDto task = taskService.deleteTask(taskId);

        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            throw new ServerException("Error in creating the User resource. Try Again.");
        }
    }


}
