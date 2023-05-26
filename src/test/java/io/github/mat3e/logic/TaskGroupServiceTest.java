package io.github.mat3e.logic;

import io.github.mat3e.model.TaskGroupRepository;
import io.github.mat3e.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {
    @Test
    @DisplayName("should throw when undone task")
    void toggleGroup_undoneTask_throwsIllegalStateException(){
        TaskRepository mockTaskRepository = taskRepositoryReturning(true);

        var toTest = new TaskGroupService(null, mockTaskRepository);

        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone tasks");
    }

    @Test
    @DisplayName("should throw when no group")
    void toggleGroup_wrongId_throwsIllegalArgumentException(){
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);

        var mockRepository = mock(TaskGroupRepository.class);

        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        var toTest = new TaskGroupService(mockRepository, mockTaskRepository);

        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    private static TaskRepository taskRepositoryReturning(final boolean results) {
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(results);
        return mockTaskRepository;
    }
}
