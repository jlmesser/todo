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

    public TaskDto addTask(TaskDto taskDto) throws Exception {
        check10();
        taskDto.setId(UUID.randomUUID()); //todo in db generation?

        return taskRepository.save(taskDto);
    }

    //check if 10 incomplete tasks already exist
    private void check10() throws Exception {
        List<TaskDto> byCompletedFalse = taskRepository.findByCompletedFalse();
        if (byCompletedFalse.size() >= 10) {
            throw new Exception("placeholder 10 exception");
        }
    }

    public TaskDto updateTask(TaskDto newTask) throws Exception {
        Optional<TaskDto> oldTask = taskRepository.findById(newTask.getId());
        if (oldTask.isPresent()) {
            if (oldTask.get().isCompleted() && !newTask.isCompleted()) {
                check10();
            }
        } else {
            throw new Exception("placeholder missing task to update/delete");
        }

        return taskRepository.save(newTask);
    }

    public void deleteTask(UUID taskId) throws Exception {
        Optional<TaskDto> oldTask = taskRepository.findById(taskId);
        if (oldTask.isEmpty()) {
            throw new Exception("placeholder missing task to update/delete");
        }
        taskRepository.deleteById(taskId);
    }


}
