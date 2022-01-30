package com.afadli.springrestapi.endpoint;

import java.time.LocalDateTime;

import com.afadli.springrestapi.model.Task;
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

        TaskController.TASKS.clear();
    }

    @Nested
    class GetTaskTests {

        @Test
        void should_return_404_on_not_existing_task_id() throws Exception {
            // When
            mvc.perform(get(TaskController.PATH + "/{id}", 1))
                    // Then
                    .andExpect(status().isNotFound());
        }

        @Test
        void should_get_task_by_id() throws Exception {
            // Given
            TaskController.TASKS.add(new Task(1L, "todo 1", "desc 1", LocalDateTime.of(2022, 1, 1, 10, 0)));

            // When
            mvc.perform(get(TaskController.PATH + "/{id}", 1))

                    // Then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("todo 1"))
                    .andExpect(jsonPath("$.description").value("desc 1"))
                    .andExpect(jsonPath("$.dateTime").value("2022-01-01T10:00:00"));
        }
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
                    .andExpect(header().stringValues("location", "http://localhost/api/v1/tasks/0"));

            assertThat(TaskController.TASKS).hasSize(1)
                    .element(0)
                    .extracting(Task::getId, Task::getName, Task::getDescription, Task::getDateTime)
                    .containsExactly(0L, "todo", "desc", LocalDateTime.parse("2023-01-02T10:00:00"));

        }
    }

    @Nested
    class DeleteTaskTests {

        @Test
        void should_return_404_on_not_existing_task_id() throws Exception {
            // Given
            TaskController.TASKS.add(new Task(1L, "todo 1", "desc 1", LocalDateTime.of(2022, 1, 1, 10, 0)));

            // When
            mvc.perform(delete(TaskController.PATH + "/{id}", 99))
                    // Then
                    .andExpect(status().isNotFound());

            assertThat(TaskController.TASKS).hasSize(1);
        }

        @Test
        void should_delete_task_by_id() throws Exception {
            // Given
            TaskController.TASKS.add(new Task(1L, "todo 1", "desc 1", LocalDateTime.of(2022, 1, 1, 10, 0)));

            // When
            mvc.perform(delete(TaskController.PATH + "/{id}", 1))
                    // Then
                    .andExpect(status().isNoContent());

            assertThat(TaskController.TASKS).hasSize(0);
        }
    }
}
