package com.example.todo_app.controller;

import com.example.todo_app.dto.TaskDto;
import com.example.todo_app.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "TaskController", description = "Controller for ToDo list app to create, edit, and delete Tasks")
@RestController
public class TaskController {

    TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @Operation(
            summary = "Retrieve all Tasks",
            description = "Get all Tasks. The response is a list of Task objects each with id, description and completion status.",
            tags = {"tasks", "get"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getTasks() {
        List<TaskDto> tasks = taskService.getTasks();

        return ResponseEntity.ok(tasks);
    }

    @Operation(
            summary = "Retrieve a Task by Id",
            description = "Get a Task object by specifying its id. The response is Task object with id, description and completion status.",
            tags = {"tasks", "get"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TaskDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping(value = "/task/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> getTask(@PathVariable UUID taskId) {
        Optional<TaskDto> optionalTaskDto = taskService.getTask(taskId);

        return optionalTaskDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a Task",
            description = "Create a Task. The response is Task object with id, description and completion status.",
            tags = {"tasks", "post"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = TaskDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PostMapping(path = "/task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto newTask, HttpServletRequest request) {
        TaskDto task = taskService.addTask(newTask);

        URI location = ServletUriComponentsBuilder.fromRequestUri(request).path("/{id}").buildAndExpand(task.getId()).toUri();
        return ResponseEntity.created(location).body(task);
    }

    @Operation(
            summary = "Update a Task",
            description = "Update a Task. The response is Task object with id, description and completion status.",
            tags = {"tasks", "put"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TaskDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PutMapping(value = "/task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto updatedTask) {
        TaskDto task = taskService.updateTask(updatedTask);

        return ResponseEntity.ok(task);
    }

    @Operation(
            summary = "Delete a Task",
            description = "Delete a Task.",
            tags = {"tasks", "delete"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @DeleteMapping(value = "/task/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable UUID taskId) {
        try {
            taskService.deleteTask(taskId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}
