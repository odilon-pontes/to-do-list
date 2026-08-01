package com.example.todolist.util;

import com.example.todolist.domain.Task;

public class TaskCreator {
    public static Task createTaskToBeSaved() {
        return Task.builder()
                .name("buy five oranges")
                .build();
    }

    public static Task createValidTask() {
        return Task.builder()
                .name("buy five oranges")
                .id(1L)
                .build();
    }

    public static Task createValidUpdateTask() {
        return Task.builder()
                .name("buy five apples")
                .id(1L)
                .build();
    }
}
