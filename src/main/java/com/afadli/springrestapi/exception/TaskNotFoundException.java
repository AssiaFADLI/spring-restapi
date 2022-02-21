package com.afadli.springrestapi.exception;

import java.text.MessageFormat;

public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(Long id) {
        super(MessageFormat.format("Task with id {0} was not found", id));
    }
}
