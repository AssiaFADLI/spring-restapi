package com.afadli.springrestapi.endpoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.afadli.springrestapi.model.Task;
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
    public static final String PATH = "/api/v1/tasks";

    public static final List<Task> TASKS = new ArrayList(0);

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable("id") Long id) {
        return TASKS.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task){
        var nexId = TASKS.stream()
                .max(Comparator.comparing(Task::getId))
                .map(Task::getId)
                .map(lastId -> lastId++)
                .orElse(0L);
        task.setId(nexId);
        TASKS.add(task);
        var createdUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(Map.of("id", nexId))
                .toUri();
        return ResponseEntity.created(createdUri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable("id") Long id, @RequestBody Task newTask) {
       return TASKS.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .map(task -> {
                    task.setName(newTask.getName());
                    task.setDescription(newTask.getDescription());
                    task.setDateTime(newTask.getDateTime());
                    return ResponseEntity.ok(task);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Long id) {
        var deleted = TASKS.removeIf(task -> task.getId().equals(id));
        if(!deleted){
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.noContent().build();
    }

}
