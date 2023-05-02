package com.kamil.todoapp.controler;

import com.kamil.todoapp.model.Task;
import com.kamil.todoapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RepositoryRestController
public class TaskControler {
    public static final Logger logger = LoggerFactory.getLogger(TaskControler.class);
    private final TaskRepository repository;

    TaskControler(final TaskRepository repository) {
        this.repository = repository;
    }

//    @RequestMapping(method = RequestMethod.GET, path = "/tasks") to samo co GetMapping
    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks(){
        logger.warn("Exposing all the Task!");
        return ResponseEntity.ok(repository.findAll());
    }
    @GetMapping("/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.warn("Custome pageable ");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }




}
