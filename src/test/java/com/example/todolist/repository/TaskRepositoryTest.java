package com.example.todolist.repository;

import com.example.todolist.domain.Task;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@DisplayName("Tests for task repository")
class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("Save persists task when successful")
    void save_PersistTask_WhenSuccessful() {
        Task taskToBeSaved = createTask();

        Task taskSaved = this.taskRepository.save(taskToBeSaved);

        Assertions.assertThat(taskSaved).isNotNull();

        Assertions.assertThat(taskSaved.getId()).isNotNull();

        Assertions.assertThat(taskSaved.getName()).isEqualTo(taskToBeSaved.getName());

    }

    @Test
    @DisplayName("Save updates task when successful")
    void save_UpdatesTask_WhenSuccessful() {
        Task taskToBeSaved = createTask();

        Task taskSaved = this.taskRepository.save(taskToBeSaved);

        taskSaved.setName("buy a cat");

        Task taskUpdate = this.taskRepository.save(taskSaved);

        Assertions.assertThat(taskUpdate).isNotNull();

        Assertions.assertThat(taskUpdate.getId()).isNotNull();

        Assertions.assertThat(taskUpdate.getName()).isEqualTo(taskSaved.getName());

    }

    @Test
    @DisplayName("Delete removes task when successful")
    void delete_RemoveTask_WhenSuccessful() {
        Task taskToBeSaved = createTask();

        Task taskSaved = this.taskRepository.save(taskToBeSaved);

        this.taskRepository.delete(taskSaved);

        Optional<Task> animeOptional = this.taskRepository.findById(taskSaved.getId());

        Assertions.assertThat(animeOptional).isEmpty();


    }

    @Test
    @DisplayName("Find by name returns list of task when successful")
    void findByName_ReturnsListOfTask_WhenSuccessful() {
        Task taskToBeSaved = createTask();

        Task taskSaved = this.taskRepository.save(taskToBeSaved);

        String name = taskSaved.getName();

        List<Task> tasks = this.taskRepository.findByName(name);

        Assertions.assertThat(tasks)
                .isNotEmpty()
                .contains(taskSaved);
    }

    @Test
    @DisplayName("Find by name returns empty list when no task is found")
    void findByName_ReturnsEmptyListOfTask_WhenTaskIsNotFound() {
        List<Task> tasks = this.taskRepository.findByName("xaxa");

        Assertions.assertThat(tasks).isEmpty();

    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {
        Task task = new Task();
//        Assertions.assertThatThrownBy(() -> this.taskRepository.save(task))
//                .isInstanceOf(ConstraintViolationException.class);
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.taskRepository.save(task))
                .withMessageContaining("The task cannot be empty");
    }

    private Task createTask() {
        return Task.builder()
                .name("buy five oranges")
                .build();
    }

}