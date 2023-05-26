package io.github.mat3e.logic;

import io.github.mat3e.TaskConfigurationProperties;
import io.github.mat3e.model.ProjectRepository;
import io.github.mat3e.model.Task;
import io.github.mat3e.model.TaskGroup;
import io.github.mat3e.model.TaskGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("Should throw illegalStateException when configured to alow just 1 group and the order undone group exist")
    void createGroup_noMultipleConfigs_And_undoneGroupExist_throwsIllegalStateException() {
        // given
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        //and
        TaskConfigurationProperties mocConfig = configurationReturning(false);
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

    @Test
    @DisplayName("Should throw illegalStateException when configuration ok and no projects for a given id")
    void createGroup_configurationOK_and_noProjects_throwIllegalArhumentException() {
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskConfigurationProperties mocConfig = configurationReturning(true);
        var toTest = new ProjectService(mockRepository, null, mocConfig);

        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // + then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("Should create a new group from project")
    void createGroup_configurationOk_existingProject_createAndSavesnewGropu(){
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);

    }


    @Test
    @DisplayName("Should throw illegalStateException when configured to alow just 1 group and no groups and np projects for a given id")
    void createGroup_noMultipleConfigs_And_noUndoneGroupExist_noProjects_throwIllegalArhumentException() {
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskGroupRepository mockGoupRepository = groupRepositoryReturning(false);
        //and
        TaskConfigurationProperties mocConfig = configurationReturning(true);
        var toTest = new ProjectService(mockRepository, mockGoupRepository, mocConfig);

        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // + then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }

    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }


    private static TaskConfigurationProperties configurationReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        var mocConfig = mock(TaskConfigurationProperties.class);
        when(mocConfig.getTemplate()).thenReturn(mockTemplate);
        return mocConfig;
    }

    private TaskGroup inMemoryGroupRepository(){
        new TaskGroupRepository() {
            private int index = 0;
            private Map<Integer, TaskGroup> map = new HashMap<>();


            @Override
            public List<TaskGroup> findAll() {
                return new ArrayList<>(map.values());
            }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
                return Optional.ofNullable(map.get(id));
            }


            // TODO: 26.05.2023  102 in memory minuta 6:22
            @Override
            public TaskGroup save(TaskGroup entity) {
                if (entity.getId() == 0) {
                    try {
                        TaskGroup.class.getDeclaredField("id").set(entity, ++index);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                }
                map.put(entity.getId(), entity);
                return entity;
            }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
                return false;
            }
        }
    }


}