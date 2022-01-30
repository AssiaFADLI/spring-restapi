package com.afadli.springrestapi.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    void should_return_404_on_not_existing_task_id() throws Exception {
        mvc
                .perform(get(TaskController.PATH + "/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_get_task_by_id() throws Exception {
        mvc
                .perform(get(TaskController.PATH + "/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("todo 2"))
                .andExpect(jsonPath("$.description").value("desc 2"))
                .andExpect(jsonPath("$.dateTime").value("2022-01-02T10:00:00"));
    }
}
