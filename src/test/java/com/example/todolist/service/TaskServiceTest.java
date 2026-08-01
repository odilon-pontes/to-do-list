package com.example.todolist.service;

import com.example.todolist.domain.Task;
import com.example.todolist.exception.BadRequestException;
import com.example.todolist.repository.TaskRepository;
import com.example.todolist.util.TaskCreator;
import com.example.todolist.util.TaskPostRequestBodyCreator;
import com.example.todolist.util.TaskPutRequestBodyCreator;
import jakarta.validation.ConstraintViolationException;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;
    @Mock
    private TaskRepository taskRepositoryMock;

    @BeforeEach
    void setUp() {
        PageImpl<Task> taskPage = new PageImpl<>(List.of(TaskCreator.createValidTask()));
        BDDMockito.when(taskRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(taskPage);

        BDDMockito.when(taskRepositoryMock.findAll())
                .thenReturn(List.of(TaskCreator.createValidTask()));

        BDDMockito.when(taskRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(TaskCreator.createValidTask()));

        BDDMockito.when(taskRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(TaskCreator.createValidTask()));

        BDDMockito.when(taskRepositoryMock.save(ArgumentMatchers.any(Task.class)))
                .thenReturn(TaskCreator.createValidTask());

        BDDMockito.doNothing().when(taskRepositoryMock).delete(ArgumentMatchers.any(Task.class));
    }

    @Test
    @DisplayName("listAll returns list of task inside page object when successful")
    void listAll_ReturnsListOfTaskInsidePageObject_WhenSuccessful() {
        String expectedName = TaskCreator.createValidTask().getName();

        Page<Task> taskPage = taskService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(taskPage).isNotNull();

        Assertions.assertThat(taskPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(taskPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllNoPageable returns list of task when successful")
    void listAllNoPageable_ReturnsListOfTask_WhenSuccessful() {
        String expectedName = TaskCreator.createValidTask().getName();

        List<Task> tasks = taskService.listAllNoPageable();

        Assertions.assertThat(tasks)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(tasks.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns task when successful")
    void findByIdOrThrowBadRequestException_ReturnsListOfTask_WhenSuccessful() {
        Long expectedId = TaskCreator.createValidTask().getId();

        Task task = taskService.findByIdOrThrowBadRequestException(1L);

        Assertions.assertThat(task)
                .isNotNull();

        Assertions.assertThat(task.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws Bad BadRequestException when task is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenTaskIsNotFound() {
        BDDMockito.when(taskRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> taskService.findByIdOrThrowBadRequestException(1L))
                .withMessageContaining("Task not found");
    }

    @Test
    @DisplayName("findByName returns list of task when successful")
    void findByName_ReturnsListOfTask_WhenSuccessful() {
        String expectedName = TaskCreator.createValidTask().getName();

        List<Task> tasks = taskService.findyByName("task");

        Assertions.assertThat(tasks)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(tasks.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of task when task is not found")
    void findByName_ReturnsEmptyListOfTask_WhenTaskIsNotFound() {
        BDDMockito.when(taskRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Task> tasks = taskService.findyByName("task");

        Assertions.assertThat(tasks)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("save returns task when successful")
    void save_ReturnsListOfTask_WhenSuccessful() {
        Task task = taskService.save(TaskPostRequestBodyCreator.createTaskPostRequestBody());

        Assertions.assertThat(task).isNotNull().isEqualTo(TaskCreator.createValidTask());
    }

    @Test
    @DisplayName("replace updates task when successful")
    void replace_Updates_WhenSuccessful() {
        Assertions.assertThatCode(() -> taskService.replace(TaskPutRequestBodyCreator.createTaskPutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete removes task when successful")
    void delete_Removes_WhenSuccessful() {
        Assertions.assertThatCode(() -> taskService.delete(1L))
                .doesNotThrowAnyException();
    }

}