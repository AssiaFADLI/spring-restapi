package com.afadli.springrestapi.endpoint;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.afadli.springrestapi.model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@ControllerAdvice
@RestController
@RequestMapping(TaskController.PATH)
public class TaskController {
    public static final String PATH = "/api/v1/tasks";
    private static Long counter = 4L;

    public static List<Task> tasks = new ArrayList<>(getTasks());

    private static List<Task> getTasks() {
        return List.of(
                new Task(1L, "todo 1", "desc 1", LocalDateTime.of(2022, 1, 1, 10, 0)),
                new Task(2L, "todo 2", "desc 2", LocalDateTime.of(2022, 1, 2, 10, 0)),
                new Task(3L, "todo 3", "desc 3", LocalDateTime.of(2022, 1, 3, 10, 0))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable("id") Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task){
        task.setId(counter);
        tasks.add(task);
        var createdUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(Map.of("id", counter++))
                .toUri();
        return ResponseEntity.created(createdUri).build();
    }


}
