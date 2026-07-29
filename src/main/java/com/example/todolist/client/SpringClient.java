package com.example.todolist.client;

import com.example.todolist.domain.Task;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Task> entity = new RestTemplate().getForEntity(
                "http://localhost:8080/tasks/{id}",
                Task.class,
                1
        );

        log.info(entity);

        Task object = new RestTemplate().getForObject("http://localhost:8080/tasks/{id}", Task.class, 2);

        log.info(object);

        Task[] tasks = new RestTemplate().getForObject("http://localhost:8080/tasks/all", Task[].class);

        log.info(Arrays.toString(tasks));

        ResponseEntity<List<Task>> exchange = new RestTemplate().exchange("http://localhost:8080/tasks/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        log.info(exchange.getBody());

//        Task task = Task.builder().name("walk to dog").build();
//        Task tasksaved = new RestTemplate().postForObject("http://localhost:8080/tasks", task, Task.class);
//
//        log.info("Task saved {}", tasksaved);

        Task buyRice = Task.builder().name("buy rice").build();
        ResponseEntity<Task> buyRiceSaved = new RestTemplate().exchange(
                "http://localhost:8080/tasks",
                HttpMethod.POST,
                new HttpEntity<>(buyRice, createJsonHeader()),
                Task.class);

        log.info("Task saved {}", buyRiceSaved);


        Task buyBeans = buyRiceSaved.getBody();


        buyBeans.setName("buy beans");
        ResponseEntity<Void> buyRiceUpdate = new RestTemplate().exchange(
                "http://localhost:8080/tasks",
                HttpMethod.PUT,
                new HttpEntity<>(buyBeans, createJsonHeader()),
                Void.class);

        log.info(buyRiceUpdate);

        ResponseEntity<Void> buyRiceDelete = new RestTemplate().exchange(
                "http://localhost:8080/tasks/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                buyBeans.getId());

        log.info(buyRiceDelete);

    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
