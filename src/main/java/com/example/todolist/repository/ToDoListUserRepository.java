package com.example.todolist.repository;

import com.example.todolist.domain.Task;
import com.example.todolist.domain.ToDoListUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoListUserRepository extends JpaRepository<ToDoListUser, Long> {
    ToDoListUser findByUsername(String username);
}
