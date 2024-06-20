package com.example.todo_app.service;

import com.example.todo_app.dto.TaskDto;
import com.example.todo_app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service which handles business logic for the To Do app
 */
@Service
public class TaskService {

    private static final int MAX_INCOMPLETE = 10;

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Get all tasks from the database. This includes complete and incomplete tasks.
     *
     * @return List of Tasks
     */
    public List<TaskDto> getTasks() {
        return taskRepository.findAll();
    }

    /**
     * Gets a specific, pre-existing task via it's unique identifier.
     *
     * @param id UUID of an existing task
     * @return Optional of TaskDto, empty if there was no Task found.
     */
    public Optional<TaskDto> getTask(UUID id) {
        return taskRepository.findById(id);
    }

    /**
     * Returns the newly created task if the maximum number of incomplete tasks has NOT been reached.
     *
     * @param taskDto represents a Task with a description, other data can be populated at this point
     * @return fully populated TaskDto
     * @throws IllegalStateException if the maximum number of incomplete tasks has been reached
     */
    public TaskDto addTask(TaskDto taskDto) throws IllegalStateException {
        check10();
        if (taskDto.getId() == null) {
            taskDto.setId(UUID.randomUUID());
        }

        return taskRepository.save(taskDto);
    }

    /**
     * Updates the task matching the UUID of the newTask
     *
     * @param newTask updated version of a pre-existing task with all fields filled in
     * @return The updated TaskDtto
     * @throws IllegalStateException    if the maximum number of incomplete tasks has been reached
     * @throws IllegalArgumentException if the Task does not exist already in the database.
     */
    public TaskDto updateTask(TaskDto newTask) throws IllegalStateException, IllegalArgumentException {
        Optional<TaskDto> oldTask = taskRepository.findById(newTask.getId());
        if (oldTask.isPresent()) {
            if (oldTask.get().isCompleted() && !newTask.isCompleted()) {
                check10();
            }
        } else {
            throw new IllegalArgumentException("ID provided does not match any existing task, please provide a valid ID.");
        }

        return taskRepository.save(newTask);
    }

    /**
     * Deletes the task matching the given UUID.
     *
     * @param taskId the unique identifier for the pre-existing task to delete.
     * @throws IllegalArgumentException if the Task does not exist already in the database.
     */
    public void deleteTask(UUID taskId) throws IllegalArgumentException {
        Optional<TaskDto> oldTask = taskRepository.findById(taskId);
        if (oldTask.isEmpty()) {
            throw new IllegalArgumentException("ID provided does not match any existing task, please provide a valid ID.");
        }
        taskRepository.deleteById(taskId);
    }

    /**
     * Throws an exception if the maximum number of incomplete tasks has been reached.
     * This method is only called when there is an attempt to add a new incomplete task or a pre-existing task is being modified to be incomplete.
     *
     * @throws IllegalStateException if the maximum number of incomplete tasks has been reached
     */
    private void check10() throws IllegalStateException {
        List<TaskDto> byCompletedFalse = taskRepository.findByCompletedFalse();
        if (byCompletedFalse.size() >= MAX_INCOMPLETE) {
            throw new IllegalStateException("Maximum 10 completed tasks reached, complete or delete an existing task before reattempting this operation.");
        }
    }


}
