package com.model.tasks;

import com.status.Status;

import java.util.Objects;

public class Task {

    private int id;
    private final String title;
    private final String description;
    private Status status;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id + ". " + title + "  " + description + " " + status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Task)) return false;
        Task task = (Task) obj;
        return title.equals(task.title) && description.equals(task.description);
    }

    public boolean equalsFull(Task task) {
        return this.id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description);
    }
}

