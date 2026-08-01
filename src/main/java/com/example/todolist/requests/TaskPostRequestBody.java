package com.example.todolist.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskPostRequestBody {
    @NotEmpty(message = "The task cannot be empty")
    private String name;
}
