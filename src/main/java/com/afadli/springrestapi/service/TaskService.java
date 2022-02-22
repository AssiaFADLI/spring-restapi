package com.afadli.springrestapi.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.afadli.springrestapi.exception.TaskNotFoundException;
import com.afadli.springrestapi.model.Task;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    public static final List<Task> TASKS = new ArrayList(0);

    public Task createTask(Task task) {
        var nexId = TASKS.stream()
                .max(Comparator.comparing(Task::getId))
                .map(Task::getId)
                .map(lastId -> lastId++)
                .orElse(0L);
        task.setId(nexId);
        TASKS.add(task);
        return task;
    }

    public Task getTask(Long id) throws TaskNotFoundException {
        return TASKS.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task updateTask(Long id, Task newTask) throws TaskNotFoundException {
        var updatedTask = getTask(id);

        updatedTask.setName(newTask.getName());
        updatedTask.setDescription(newTask.getDescription());
        updatedTask.setDateTime(newTask.getDateTime());

        return updatedTask;
    }

    public void deleteTask(Long id) throws TaskNotFoundException {
        var task = getTask(id);
        TASKS.remove(task);
    }
}
