package com.afadli.springrestapi.endpoint;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.afadli.springrestapi.model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
@RequestMapping(TaskController.PATH)
public class TaskController {
    public static final String PATH = "/api/v1/tasks";

    public static List<Task> tasks = List.of(
            new Task(1L, "todo 1", "desc 1", LocalDateTime.of(2022, 1, 1, 10, 0)),
            new Task(2L, "todo 2", "desc 2", LocalDateTime.of(2022, 1, 2, 10, 0)),
            new Task(3L, "todo 3", "desc 3", LocalDateTime.of(2022, 1, 3, 10, 0))
    );

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable("id") Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }


}
