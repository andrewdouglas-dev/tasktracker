package com.github;

import java.time.LocalDateTime;

import com.github.types.Status;

public class Task {
    public String description;
    public LocalDateTime createdOn;
    public LocalDateTime updatedOn;
    public Status status;

    public Task(String description) {
        this.description = description;
        this.createdOn = LocalDateTime.now();
        setToDo();
    }

    @Override
    public String toString() {
        return this.description + " | " + this.status; 
    }

    public void setToDo() {
        this.status = Status.TODO;
    }

    public void setInProgress() {
        this.status = Status.INPROGRESS;
    }

    public void setDone() {
        this.status = Status.DONE;
    }

    public void updateDescription(String desc) {
        this.description = desc;
        this.updatedOn = LocalDateTime.now();
    }
}
