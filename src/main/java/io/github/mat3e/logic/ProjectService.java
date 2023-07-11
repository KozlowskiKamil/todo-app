package io.github.mat3e.logic;

import io.github.mat3e.TaskConfigurationProperties;
import io.github.mat3e.model.Project;
import io.github.mat3e.model.ProjectRepository;
import io.github.mat3e.model.TaskGroupRepository;
import io.github.mat3e.model.projection.GroupReadModel;
import io.github.mat3e.model.projection.GroupTaskWriteModel;
import io.github.mat3e.model.projection.GroupWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@Service
public class ProjectService {
    private final ProjectRepository repository;
    private final TaskGroupRepository taskGroupRepository;
    private TaskGroupService taskGroupService;
    private final TaskConfigurationProperties config;

    public ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskGroupService taskGroupService, final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskGroupService = taskGroupService;
        this.config = config;
    }
    public ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(final Project toSave) {
        return repository.save(toSave);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        return repository.findById(projectId).map(project -> {
            var targetGroup = new GroupWriteModel();
            targetGroup.setDescription(project.getDescription());

            targetGroup.setTasks(project.getSteps().stream().map(projectStep -> {
                var task = new GroupTaskWriteModel();
                task.setDescription(projectStep.getDescription());
                task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                return task;
            }).collect(Collectors.toSet()));
            return taskGroupService.createGroup(targetGroup);
        }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}
