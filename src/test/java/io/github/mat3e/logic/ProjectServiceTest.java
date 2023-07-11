package io.github.mat3e.logic;

import io.github.mat3e.TaskConfigurationProperties;
import io.github.mat3e.model.*;
import io.github.mat3e.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        var toTest = new ProjectService(null, mockGroupRepository, null, mockConfig);

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

        TaskConfigurationProperties mockConfig = configurationReturning(true);
        var toTest = new ProjectService(mockRepository, null, null, mockConfig);

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
        var today = LocalDate.now().atStartOfDay();
        //and

        var project = projectWith("bar", Set.of(-1, -2));

        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        //and
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        var serviceWithInMemRepo = dummyGroupService(inMemoryGroupRepo);
        int countBeforeCall = inMemoryGroupRepo.count();
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);

        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo, serviceWithInMemRepo, mockConfig);

        GroupReadModel result = toTest.createGroup(today, 1);

        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).anyMatch(task -> task.getDescription().equals("foo"));


        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepo.count());
    }

    private static TaskGroupService dummyGroupService(InMemoryGroupRepository inMemoryGroupRepo) {
        return new TaskGroupService(inMemoryGroupRepo, null);
    }


    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                }).collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
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
        var toTest = new ProjectService(mockRepository, mockGoupRepository, null, mocConfig);

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

    private InMemoryGroupRepository inMemoryGroupRepository() {return new InMemoryGroupRepository();}

    private static class InMemoryGroupRepository implements  TaskGroupRepository{
        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }


        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(final Integer id) {
            return Optional.ofNullable(map.get(id));
        }


        @Override
        public TaskGroup save(final TaskGroup entity) {
            if (entity.getId() == 0) {
                try {
                   var field = TaskGroup.class.getDeclaredField("id");
                   field.setAccessible(true);
                   field.set(entity, ++index);

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(final Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }
    }
}