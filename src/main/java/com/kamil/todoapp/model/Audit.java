package com.kamil.todoapp.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@Embeddable
class Audit {
    private LocalDateTime created_on;
    private LocalDateTime updated_on;

    @PrePersist
    void prePersist(){
        created_on = LocalDateTime.now();
    }
    @PreUpdate
    void preMerge(){
        updated_on = LocalDateTime.now();
    }

}
