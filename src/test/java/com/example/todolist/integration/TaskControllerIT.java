package com.example.todolist.integration;

import com.example.todolist.domain.Task;
import com.example.todolist.domain.ToDoListUser;
import com.example.todolist.repository.TaskRepository;
import com.example.todolist.repository.ToDoListUserRepository;
import com.example.todolist.requests.TaskPostRequestBody;
import com.example.todolist.util.TaskCreator;
import com.example.todolist.util.TaskPostRequestBodyCreator;
import com.example.todolist.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TaskControllerIT {
    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;
    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ToDoListUserRepository toDoListUserRepository;

    private static final ToDoListUser USER = ToDoListUser.builder()
            .name("to-do-list")
            .password("$2a$10$n8.AFRTHyOjpVrjOpPpMbOazxYTeiR.Bwa/hgc7RP5sBXVrI.XXo6")
            .username("dev")
            .authorities("ROLE_USER")
            .build();
    private static final ToDoListUser ADMIN = ToDoListUser.builder()
            .name("to-do-list")
            .password("$2a$10$n8.AFRTHyOjpVrjOpPpMbOazxYTeiR.Bwa/hgc7RP5sBXVrI.XXo6")
            .username("odilon")
            .authorities("ROLE_USER,ROLE_ADMIN")
            .build();
    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("dev", "user");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("admin", "admin");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }
    @Test
    @DisplayName("list returns list of task inside page object when successful")
    void list_ReturnsListOfTaskInsidePageObject_WhenSuccessful() {
        Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
        toDoListUserRepository.save(USER);
        String expectedName = savedTask.getName();

        PageableResponse<Task> taskPage = testRestTemplateRoleUser.exchange("/tasks", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Task>>() {
                }).getBody();

        Assertions.assertThat(taskPage).isNotNull();
        Assertions.assertThat(taskPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(taskPage.toList().get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("listAll returns list of task when successful")
    void listAll_ReturnsListOfTask_WhenSuccessful() {
        Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
        toDoListUserRepository.save(USER);

        String expectedName = savedTask.getName();

        List<Task> tasks = testRestTemplateRoleUser.exchange("/tasks/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {
                }).getBody();

        Assertions.assertThat(tasks)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(tasks.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns task when successful")
    void findById_ReturnsListOfTask_WhenSuccessful() {
        Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
        toDoListUserRepository.save(USER);

        Long expectedId = savedTask.getId();

        Task task = testRestTemplateRoleUser.getForObject("/tasks/{id}", Task.class, expectedId);

        Assertions.assertThat(task)
                .isNotNull();

        Assertions.assertThat(task.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of task when successful")
    void findByName_ReturnsListOfTask_WhenSuccessful() {
        Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
        toDoListUserRepository.save(USER);


        String expectedName = savedTask.getName();

        String url = String.format("/tasks/find?name=%s", expectedName);

        List<Task> tasks = testRestTemplateRoleUser.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {
                }).getBody();

        Assertions.assertThat(tasks)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(tasks.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of task when task is not found")
    void findByName_ReturnsEmptyListOfTask_WhenTaskIsNotFound() {
        toDoListUserRepository.save(USER);

        List<Task> tasks = testRestTemplateRoleUser.exchange("/tasks/find?name=hi", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {
                }).getBody();

        Assertions.assertThat(tasks)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns task when successful")
    void save_ReturnsListOfTask_WhenSuccessful() {
        toDoListUserRepository.save(USER);

        TaskPostRequestBody taskPostRequestBody = TaskPostRequestBodyCreator.createTaskPostRequestBody();
        ResponseEntity<Task> taskResponseEntity = testRestTemplateRoleUser.postForEntity("/tasks",
                taskPostRequestBody,
                Task.class);


        Assertions.assertThat(taskResponseEntity).isNotNull();
        Assertions.assertThat(taskResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(taskResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(taskResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace updates task when successful")
    void replace_Updates_WhenSuccessful() {
        Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
        toDoListUserRepository.save(USER);


        savedTask.setName("new name");
        ResponseEntity<Void> taskResponseEntity = testRestTemplateRoleUser.exchange(
                "/tasks",
                HttpMethod.PUT,
                new HttpEntity<>(savedTask),
                Void.class
        );

        Assertions.assertThat(taskResponseEntity).isNotNull();
        Assertions.assertThat(taskResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes task when successful")
    void delete_Removes_WhenSuccessful() {
        Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
        toDoListUserRepository.save(ADMIN);


        ResponseEntity<Void> taskResponseEntity = testRestTemplateRoleAdmin.exchange(
                "/tasks/admin/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedTask.getId()
        );

        Assertions.assertThat(taskResponseEntity).isNotNull();
        Assertions.assertThat(taskResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns 403  when user is not admin")
    void delete_Returns403_WhenUserIsNotAdmin() {
        Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
        toDoListUserRepository.save(USER);


        ResponseEntity<Void> taskResponseEntity = testRestTemplateRoleUser.exchange(
                "/tasks/admin/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedTask.getId()
        );

        Assertions.assertThat(taskResponseEntity).isNotNull();

        Assertions.assertThat(taskResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


}
