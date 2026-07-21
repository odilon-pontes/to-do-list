package com.example.todolist.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TaskPostRequestBody {
    @NotEmpty(message = "The task cannot be empty")
    private String name;
}
