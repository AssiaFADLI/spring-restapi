package com.afadli.springrestapi.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.afadli.springrestapi.model.Task;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    public static final List<Task> TASKS = new ArrayList(0);

    public Task createTask(Task task){
        var nexId = TASKS.stream()
                .max(Comparator.comparing(Task::getId))
                .map(Task::getId)
                .map(lastId -> lastId++)
                .orElse(0L);
        task.setId(nexId);
        TASKS.add(task);
        return task;
    }
}
