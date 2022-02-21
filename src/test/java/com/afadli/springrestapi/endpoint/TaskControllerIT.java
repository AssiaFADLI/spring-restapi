package com.afadli.springrestapi.endpoint;

import java.time.LocalDateTime;

import com.afadli.springrestapi.model.Task;
import com.afadli.springrestapi.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIT {

    private MockMvc mvc;
    @Autowired
    protected WebApplicationContext context;
    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        TaskService.TASKS.clear();
    }

    @Nested
    class CreateTaskTests {

        @Test
        void should_create_task() throws Exception {
            // Given
            var body = objectMapper.createObjectNode()
                    .put("name", "todo")
                    .put("description", "desc")
                    .put("dateTime", "2023-01-02T10:00:00")
                    .toString();

            // When
            mvc.perform(post(TaskController.PATH)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(body))
                    // Then
                    .andExpect(status().isCreated())
                    .andExpect(header().stringValues("location", "http://localhost/api/v2/tasks/0"));

            assertThat(TaskService.TASKS).hasSize(1)
                    .element(0)
                    .extracting(Task::getId, Task::getName, Task::getDescription, Task::getDateTime)
                    .containsExactly(0L, "todo", "desc", LocalDateTime.parse("2023-01-02T10:00:00"));

        }
    }
}
