package io.github.mat3e.controller;

import io.github.mat3e.TaskConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
class InfoController {
    private DataSourceProperties dataSource;
    private TaskConfigurationProperties myProp;

    InfoController(final DataSourceProperties dataSource, final TaskConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    @GetMapping("/url")
    String url() {
        return dataSource.getUrl();
    }

    @GetMapping("/prop")
    boolean myProp() {
        if (myProp.getTemplate() != null) {
            return myProp.getTemplate().isAllowMultipleTasks();
        }
        return false; // lub inna domyślna wartość, jeśli template jest null
    }

//    @GetMapping("/prop")
//    boolean myProp() {
//        return myProp.getTemplate().isAllowMultipleTasks();
//    }
}
