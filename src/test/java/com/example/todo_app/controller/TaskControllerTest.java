package com.example.todo_app.controller;

import com.example.todo_app.dto.TaskDto;
import com.example.todo_app.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    public static final TaskDto TASK_DTO = new TaskDto("task1", UUID.fromString("9165c11b-c339-443b-9da2-0d0831d8435e"), false);
    @Mock
    TaskService taskService;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new TaskController(taskService)).build();
    }

    @Test
    void getTasks_emptyDb_emptyList() throws Exception {
        Mockito.when(taskService.getTasks()).thenReturn(List.of());
        mockMvc.perform(get("/tasks")).andExpect(MockMvcResultMatchers.content().json("[]")).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void getTasks_tasksPresent_validList() throws Exception {
        Mockito.when(taskService.getTasks()).thenReturn(List.of(TASK_DTO));
        mockMvc.perform(get("/tasks")).andExpect(MockMvcResultMatchers.content().json("""
                [
                    {
                        "description": "task1",
                        "id": "9165c11b-c339-443b-9da2-0d0831d8435e",
                        "completed": false
                    }
                ]
                """)).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void getTask_isPresent_validTask() throws Exception {
        Mockito.when(taskService.getTask(TASK_DTO.getId())).thenReturn(Optional.of(TASK_DTO));
        mockMvc.perform(get("/task/" + TASK_DTO.getId().toString())).andExpect(MockMvcResultMatchers.content().json("""
                    {
                        "description": "task1",
                        "id": "9165c11b-c339-443b-9da2-0d0831d8435e",
                        "completed": false
                    }
                """)).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void getTask_isNotPresent_error() throws Exception {
        Mockito.when(taskService.getTask(TASK_DTO.getId())).thenReturn(Optional.empty());
        mockMvc.perform(get("/task/" + TASK_DTO.getId().toString())).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createTask_success() throws Exception {
        Mockito.when(taskService.addTask(Mockito.any())).thenReturn(TASK_DTO);
        mockMvc.perform(post("/task").content("""
                 {"description": "task1"}
                """).characterEncoding("utf-8").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(MockMvcResultMatchers.content().json("""
                    {
                        "description": "task1",
                        "id": "9165c11b-c339-443b-9da2-0d0831d8435e",
                        "completed": false
                    }
                """)).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void updateTask_success() throws Exception {
        Mockito.when(taskService.updateTask(Mockito.any())).thenReturn(TASK_DTO);
        mockMvc.perform(put("/task").content("""
                                     {
                    "description": "task1",
                    "id": "9165c11b-c339-443b-9da2-0d0831d8435e",
                    "completed": false
                }
                """).characterEncoding("utf-8").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(MockMvcResultMatchers.content().json("""
                    {
                        "description": "task1",
                        "id": "9165c11b-c339-443b-9da2-0d0831d8435e",
                        "completed": false
                    }
                """)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteTask_isNotPresent_error() throws Exception {
        Mockito.doThrow(new IllegalArgumentException()).when(taskService).deleteTask(TASK_DTO.getId());
        mockMvc.perform(delete("/task/" + TASK_DTO.getId().toString())).andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void deleteTask_isPresent_success() throws Exception {
        mockMvc.perform(delete("/task/" + TASK_DTO.getId().toString())).andExpect(MockMvcResultMatchers.status().isOk());
    }
}