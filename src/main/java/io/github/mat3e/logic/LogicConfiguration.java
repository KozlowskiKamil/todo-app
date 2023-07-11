package io.github.mat3e.logic;

import io.github.mat3e.TaskConfigurationProperties;
import io.github.mat3e.model.ProjectRepository;
import io.github.mat3e.model.TaskGroupRepository;
import io.github.mat3e.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicConfiguration {
    @Bean
    ProjectService projectService(
            ProjectRepository repository,
            TaskGroupRepository taskGroupRepository,
            TaskConfigurationProperties config) {
        return new ProjectService(repository, taskGroupRepository, config);
    }

    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository repository,
            final TaskRepository taskRepository) {
        return new TaskGroupService(repository, taskRepository);
    }
}
