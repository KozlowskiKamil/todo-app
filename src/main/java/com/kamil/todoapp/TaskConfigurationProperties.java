package com.kamil.todoapp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("task")
public class TaskConfigurationProperties {
    private Template template;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(final Template template) {
        this.template = template;
    }

    public static class Template {
        private boolean allowMultipleTask;

        public boolean isAllowMultipleTask() {
            return allowMultipleTask;
        }

        public void setAllowMultipleTask(final boolean allowMultipleTask) {
            this.allowMultipleTask = allowMultipleTask;
        }
    }
}
