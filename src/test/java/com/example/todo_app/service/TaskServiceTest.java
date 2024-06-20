package com.example.todo_app.service;

import com.example.todo_app.dto.TaskDto;
import com.example.todo_app.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    public static final TaskDto INCOMPLETE_TASK = new TaskDto("task1", UUID.randomUUID(), false);
    public static final TaskDto COMPLETE_TASK = new TaskDto("task2", UUID.randomUUID(), true);
    public static final List<TaskDto> TEN_COMPLETED_TASKS_LIST = List.of(createCompletedTask(), createCompletedTask(), createCompletedTask(), createCompletedTask(), createCompletedTask(), createCompletedTask(), createCompletedTask(), createCompletedTask(), createCompletedTask(), createCompletedTask());

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskService taskService;

    @Test
    void getTasks_emptyDb_emptyList() {
        List<TaskDto> actual = taskService.getTasks();
        List<TaskDto> expected = List.of();
        assertEquals(expected, actual);
    }

    @Test
    void getTasks_tasksPresent_validList() {
        TaskDto task1 = new TaskDto("task1", UUID.randomUUID(), false);
        TaskDto task2 = createCompletedTask();
        List<TaskDto> taskDtoList = List.of(task1, task2);
        Mockito.when(taskRepository.findAll()).thenReturn(taskDtoList);
        List<TaskDto> actual = taskService.getTasks();
        assertEquals(taskDtoList, actual);
    }

    private static TaskDto createCompletedTask() {
        UUID id = UUID.randomUUID();
        return new TaskDto("task_" + id, id, true);
    }

    @Test
    void getTask_isPresent_validTask() {
        UUID taskId = UUID.randomUUID();
        Optional<TaskDto> task1 = Optional.of(new TaskDto("task1", taskId, false));
        Mockito.when(taskRepository.findById(taskId)).thenReturn(task1);
        Optional<TaskDto> actual = taskService.getTask(taskId);
        assertEquals(task1, actual);
    }

    @Test
    void getTask_isNotPresent_emptyOptional() {
        UUID taskId = UUID.randomUUID();
        Optional<TaskDto> task1 = Optional.empty();
        Mockito.when(taskRepository.findById(taskId)).thenReturn(task1);
        Optional<TaskDto> actual = taskService.getTask(taskId);
        assertEquals(task1, actual);
    }

    @Test
    void addTask_maxNotReached_success() {
        Mockito.when(taskRepository.save(INCOMPLETE_TASK)).thenReturn(INCOMPLETE_TASK);
        Mockito.when(taskRepository.findByCompletedFalse()).thenReturn(List.of());
        TaskDto actual = taskService.addTask(INCOMPLETE_TASK);
        assertEquals(INCOMPLETE_TASK, actual);
        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any()); //todo more verifying
        Mockito.verify(taskRepository, Mockito.times(1)).findByCompletedFalse();
    }

    @Test
    void addTask_maxReached_failure() {
        Mockito.when(taskRepository.findByCompletedFalse()).thenReturn(TEN_COMPLETED_TASKS_LIST);

        assertThrows(IllegalStateException.class, () -> taskService.addTask(INCOMPLETE_TASK));
        Mockito.verify(taskRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(taskRepository, Mockito.times(1)).findByCompletedFalse();
    }

    @Test
    void updateTask_taskNotPresent_failure() {
        Mockito.when(taskRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> taskService.updateTask(INCOMPLETE_TASK));
        Mockito.verify(taskRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void updateTask_maxReachedUpdateIncomplete_failure() {
        Mockito.when(taskRepository.findByCompletedFalse()).thenReturn(TEN_COMPLETED_TASKS_LIST);
        Mockito.when(taskRepository.findById(COMPLETE_TASK.getId())).thenReturn(Optional.of(COMPLETE_TASK));
        TaskDto updatedTask = new TaskDto(COMPLETE_TASK.getDescription(), COMPLETE_TASK.getId(), false);
        assertThrows(IllegalStateException.class, () -> taskService.updateTask(updatedTask));
        Mockito.verify(taskRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(taskRepository, Mockito.times(1)).findByCompletedFalse();
    }

    @Test
    void updateTask_maxNotReachedUpdateIncomplete_success() {
        Mockito.when(taskRepository.findByCompletedFalse()).thenReturn(List.of());
        Mockito.when(taskRepository.findById(COMPLETE_TASK.getId())).thenReturn(Optional.of(COMPLETE_TASK));
        TaskDto updatedTask = new TaskDto(COMPLETE_TASK.getDescription(), COMPLETE_TASK.getId(), false);
        Mockito.when(taskRepository.save(updatedTask)).thenReturn(updatedTask);

        TaskDto actual = taskService.updateTask(updatedTask);

        assertEquals(updatedTask, actual);

        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(taskRepository, Mockito.times(1)).findByCompletedFalse();
    }

    @Test
    void updateTask_maxNotReachedBothComplete_success() {
        Mockito.when(taskRepository.findById(COMPLETE_TASK.getId())).thenReturn(Optional.of(COMPLETE_TASK));
        TaskDto updatedTask = new TaskDto("new desc", COMPLETE_TASK.getId(), COMPLETE_TASK.isCompleted());
        Mockito.when(taskRepository.save(updatedTask)).thenReturn(updatedTask);

        TaskDto actual = taskService.updateTask(updatedTask);

        assertEquals(updatedTask, actual);

        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(taskRepository, Mockito.times(0)).findByCompletedFalse();
    }

    @Test
    void updateTask_maxNotReachedBothIncomplete_success() {
        Mockito.when(taskRepository.findById(INCOMPLETE_TASK.getId())).thenReturn(Optional.of(INCOMPLETE_TASK));
        TaskDto updatedTask = new TaskDto("new desc", INCOMPLETE_TASK.getId(), INCOMPLETE_TASK.isCompleted());
        Mockito.when(taskRepository.save(updatedTask)).thenReturn(updatedTask);

        TaskDto actual = taskService.updateTask(updatedTask);

        assertEquals(updatedTask, actual);

        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(taskRepository, Mockito.times(0)).findByCompletedFalse();
    }

    @Test
    void deleteTask_isPresent_success() {
        Mockito.when(taskRepository.findById(COMPLETE_TASK.getId())).thenReturn(Optional.of(COMPLETE_TASK));
        taskService.deleteTask(COMPLETE_TASK.getId());
        Mockito.verify(taskRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById(Mockito.any());
    }

    @Test
    void deleteTask_isNotPresent_failure() {
        Mockito.when(taskRepository.findById(COMPLETE_TASK.getId())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> taskService.deleteTask(COMPLETE_TASK.getId()));
        Mockito.verify(taskRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(taskRepository, Mockito.times(0)).deleteById(Mockito.any());
    }
}