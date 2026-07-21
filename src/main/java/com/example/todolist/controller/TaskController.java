package com.example.todolist.controller;

import com.example.todolist.domain.Task;
import com.example.todolist.requests.TaskPostRequestBody;
import com.example.todolist.requests.TaskPutRequestBody;
import com.example.todolist.service.TaskService;
import com.example.todolist.util.DateUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("tasks")
@Log4j2
@RequiredArgsConstructor
public class TaskController {
    private final DateUtil dateUtil;
    private final TaskService taskService;

    //localhost:8080/task/list
    @GetMapping
    public ResponseEntity<List<Task>> list() {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(taskService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(taskService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Task>> findByName(@RequestParam String name) {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(taskService.findyByName(name));
    }

    @PostMapping
    public ResponseEntity<Task> save(@RequestBody @Valid TaskPostRequestBody taskPostRequestBody) {
        return new ResponseEntity<>(taskService.save(taskPostRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Task> replace(@RequestBody TaskPutRequestBody taskPutRequestBody) {
        taskService.replace(taskPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
