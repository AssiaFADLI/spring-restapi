package com.afadli.springrestapi.endpoint;

import java.util.Map;

import com.afadli.springrestapi.model.Task;
import com.afadli.springrestapi.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@ControllerAdvice
@RestController
@RequestMapping(TaskController.PATH)
public class TaskController {
    public static final String PATH = "/api/v2/tasks";

    @Autowired
    TaskService taskService;

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        var createdTask = this.taskService.createTask(task);
        var createdUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(Map.of("id", createdTask.getId()))
                .toUri();
        return ResponseEntity.created(createdUri).build();
    }

}
