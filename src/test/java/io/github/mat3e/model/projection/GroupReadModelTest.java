package io.github.mat3e.model.projection;

import io.github.mat3e.model.Task;
import io.github.mat3e.model.TaskGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {
    @Test
    @DisplayName("shuld create null deadline for grup")
    void constructor_noDeadlines_createsNulldedline() {
        var source = new TaskGroup();
        source.setDescription("taskTest");
        source.setTasks(Set.of(new Task("task1", null)));

        var result = new GroupReadModel(source);
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }

}