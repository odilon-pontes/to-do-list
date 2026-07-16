package com.example.todolist.repository;

import com.example.todolist.domain.Task;

import java.util.List;

public interface TaskRepository {
    List<Task> listAll();
}
