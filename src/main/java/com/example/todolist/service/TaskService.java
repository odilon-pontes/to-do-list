package com.example.todolist.service;

import com.example.todolist.domain.Task;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TaskService {
    // private final TaskRepository taskRepository;
    private static List<Task> tasks;

    static {
        tasks = new ArrayList<>(List.of(new Task(1L, "buy rice"), new Task(2L, "study mathematics"), new Task(3L, "work out at the gym")));
    }
    public List<Task> listAll() {
        return tasks;
    }

    public Task findById(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task not found"));
    }

    public Task save(Task task) {
        task.setId(ThreadLocalRandom.current().nextLong(3, 1000000));
        tasks.add(task);
        return task;
    }

    public void delete(Long id) {
        tasks.remove(findById(id));
    }

    public void replace(Task task) {
        delete(task.getId());
        tasks.add(task);
    }
}
