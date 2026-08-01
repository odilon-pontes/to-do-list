package com.example.todolist.util;

import com.example.todolist.requests.TaskPostRequestBody;
import com.example.todolist.requests.TaskPutRequestBody;

public class TaskPutRequestBodyCreator {
    public static TaskPutRequestBody createTaskPutRequestBody() {
        return TaskPutRequestBody.builder()
                .id(TaskCreator.createValidUpdateTask().getId())
                .name(TaskCreator.createValidUpdateTask().getName())
                .build();
    }
}
