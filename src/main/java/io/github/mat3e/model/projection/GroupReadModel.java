package io.github.mat3e.model.projection;

import io.github.mat3e.model.Task;
import io.github.mat3e.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String description;
    /**
     * Deadline form the latest task in group.
     */
    private LocalDateTime deadline;
    private Set<GroupTaskReadModel> tasks;

    public GroupReadModel(TaskGroup source) {
        id =source.getId();
        description = source.getDescription();
        source.getTasks().stream()
                .map(Task::getDeadline)
                .max(LocalDateTime::compareTo)
                .ifPresent(date -> deadline = date);
        tasks = source.getTasks().stream()
                .map(GroupTaskReadModel::new)
                .collect(Collectors.toSet());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }
}
