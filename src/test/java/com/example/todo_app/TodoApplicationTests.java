package com.example.todo_app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

//@SpringBootTest //todo not sure if it's an issue that the test won't work with the full context
@ExtendWith(MockitoExtension.class)
class TodoApplicationTests {

	@MockBean
	TaskRepository taskRepository;


	@Test
	void contextLoads() {
	}

}
