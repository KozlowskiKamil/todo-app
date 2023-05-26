package io.github.mat3e.logic;

import io.github.mat3e.TaskConfigurationProperties;
import io.github.mat3e.model.TaskGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("Should throw illegalStateException when configured to alow just 1 group and the order undone group exist")
    void createGroup_noMultipleConfigs_And_undoneGroupExist_throwsIllegalStateException() {
        // given
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(false);
        var mocConfig = mock(TaskConfigurationProperties.class);
        when(mocConfig.getTemplate()).thenReturn(mockTemplate);
        var toTest = new ProjectService(null, mockGroupRepository, mocConfig);

            // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // + then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");

/*         assertThatIllegalStateException().isThrownBy( () -> toTest
                .createGroup(LocalDateTime.now(), 0));
       assertThatThrownBy(() -> toTest.createGroup(LocalDateTime.now(), 0))
                .isInstanceOf(IllegalStateException.class);              to samo ale inaczej zapisane*/
    }
}