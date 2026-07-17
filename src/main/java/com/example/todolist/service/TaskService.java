package com.example.todolist.service;

import com.example.todolist.domain.Task;
import com.example.todolist.repository.TaskRepository;
import com.example.todolist.requests.TaskPostRequestBody;
import com.example.todolist.requests.TaskPutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public List<Task> listAll() {
        return taskRepository.findAll();
    }

    public Task findByIdOrThrowBadRequestException(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task not found"));
    }

    public Task save(TaskPostRequestBody taskPostRequestBody) {
        return taskRepository.save(Task.builder().name(taskPostRequestBody.getName()).build());
    }

    public void delete(Long id) {
        taskRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(TaskPutRequestBody taskPutRequestBody) {
        Task savedTask = findByIdOrThrowBadRequestException(taskPutRequestBody.getId());
        Task task = Task.builder()
                .id(savedTask.getId())
                .name(taskPutRequestBody.getName())
                .build();
        taskRepository.save(task);
    }
}
