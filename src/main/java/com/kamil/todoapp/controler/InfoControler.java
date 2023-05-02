package com.kamil.todoapp.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoControler {
    @Autowired
    private DataSourceProperties dataSource;
    @Value("${task.allowMultipleTaskFromTemplate}")
    private String myProp;
    @GetMapping("/info/url")
    String url(){
        return dataSource.getUrl();
    }
    @GetMapping("/info/prop")
    String myProp(){
        return myProp;
    }

}
