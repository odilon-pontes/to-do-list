package com.example.todolist.controller;

import com.example.todolist.domain.Task;
import com.example.todolist.requests.TaskPostRequestBody;
import com.example.todolist.requests.TaskPutRequestBody;
import com.example.todolist.service.TaskService;
import com.example.todolist.util.TaskCreator;
import com.example.todolist.util.TaskPostRequestBodyCreator;
import com.example.todolist.util.TaskPutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class TaskControllerTest {
    @InjectMocks
    private TaskController taskController;
    @Mock
    private TaskService taskServiceMock;

    @BeforeEach
    void setUp() {
        PageImpl<Task> taskPage = new PageImpl<>(List.of(TaskCreator.createValidTask()));
        BDDMockito.when(taskServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(taskPage);

        BDDMockito.when(taskServiceMock.listAllNoPageable())
                .thenReturn(List.of(TaskCreator.createValidTask()));

        BDDMockito.when(taskServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(TaskCreator.createValidTask());

        BDDMockito.when(taskServiceMock.findyByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(TaskCreator.createValidTask()));

        BDDMockito.when(taskServiceMock.save(ArgumentMatchers.any(TaskPostRequestBody.class)))
                .thenReturn(TaskCreator.createValidTask());

        BDDMockito.doNothing().when(taskServiceMock).replace(ArgumentMatchers.any(TaskPutRequestBody.class));
        BDDMockito.doNothing().when(taskServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("list returns list of task inside page object when successful")
    void list_ReturnsListOfTaskInsidePageObject_WhenSuccessful() {
        String expectedName = TaskCreator.createValidTask().getName();

        Page<Task> taskPage = taskController.list(null).getBody();

        Assertions.assertThat(taskPage).isNotNull();

        Assertions.assertThat(taskPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(taskPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAll returns list of task when successful")
    void listAll_ReturnsListOfTask_WhenSuccessful() {
        String expectedName = TaskCreator.createValidTask().getName();

        List<Task> tasks = taskController.listAll().getBody();

        Assertions.assertThat(tasks)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(tasks.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns task when successful")
    void findById_ReturnsListOfTask_WhenSuccessful() {
        Long expectedId = TaskCreator.createValidTask().getId();

        Task task = taskController.findById(1L).getBody();

        Assertions.assertThat(task)
                .isNotNull();

        Assertions.assertThat(task.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of task when successful")
    void findByName_ReturnsListOfTask_WhenSuccessful() {
        String expectedName = TaskCreator.createValidTask().getName();

        List<Task> tasks = taskController.findByName("task").getBody();

        Assertions.assertThat(tasks)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(tasks.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of task when task is not found")
    void findByName_ReturnsEmptyListOfTask_WhenTaskIsNotFound() {
        BDDMockito.when(taskServiceMock.findyByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Task> tasks = taskController.findByName("task").getBody();

        Assertions.assertThat(tasks)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("save returns task when successful")
    void save_ReturnsListOfTask_WhenSuccessful() {
        Task task = taskController.save(TaskPostRequestBodyCreator.createTaskPostRequestBody()).getBody();

        Assertions.assertThat(task).isNotNull().isEqualTo(TaskCreator.createValidTask());
    }

    @Test
    @DisplayName("replace updates task when successful")
    void replace_Updates_WhenSuccessful() {

        Assertions.assertThatCode(() -> taskController.replace(TaskPutRequestBodyCreator.createTaskPutRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = taskController.replace(TaskPutRequestBodyCreator.createTaskPutRequestBody());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes task when successful")
    void delete_Removes_WhenSuccessful() {

        Assertions.assertThatCode(() -> taskController.delete(1L))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = taskController.delete(1L);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}