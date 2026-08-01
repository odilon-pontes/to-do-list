package com.example.todolist.util;

import com.example.todolist.domain.Task;
import com.example.todolist.requests.TaskPostRequestBody;

public class TaskPostRequestBodyCreator {
    public static TaskPostRequestBody createTaskPostRequestBody() {
        return TaskPostRequestBody.builder()
                .name(TaskCreator.createTaskToBeSaved().getName())
                .build();
    }
}
