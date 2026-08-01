package com.example.todolist.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskPutRequestBody {
    private Long id;
    private String name;
}
