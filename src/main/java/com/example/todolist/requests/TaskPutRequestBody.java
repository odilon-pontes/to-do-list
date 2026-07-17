package com.example.todolist.requests;

import lombok.Data;

@Data
public class TaskPutRequestBody {
    private Long id;
    private String name;
}
