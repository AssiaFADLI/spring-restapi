package com.afadli.springrestapi.endpoint;

import java.util.Map;

import com.afadli.springrestapi.exception.TaskNotFoundException;
import com.afadli.springrestapi.model.Task;
import com.afadli.springrestapi.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        return ResponseEntity
                .created(createdUri)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable("id") Long id) {
        try {
            var task = taskService.getTask(id);
            return ResponseEntity.ok(task);
        } catch (TaskNotFoundException e) {
            return handleNotFoundException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable("id") Long id, @RequestBody Task newTask) {
        try {
            var task = taskService.updateTask(id, newTask);
            return ResponseEntity.ok(task);
        } catch (TaskNotFoundException e) {
            return handleNotFoundException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity
                    .noContent()
                    .build();
        } catch (TaskNotFoundException e) {
            return handleNotFoundException(e);
        }
    }

    private static ResponseEntity<TaskNotFoundException> handleNotFoundException(TaskNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e);
    }

}
