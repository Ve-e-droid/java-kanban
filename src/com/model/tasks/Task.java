package com.model.tasks;

import com.adapter.DurationAdapter;
import com.adapter.LocalDateTimeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private int id;
    private final String title;
    private final String description;

    private Status status = Status.NEW;
    @JsonAdapter(DurationAdapter.class)
    private Duration duration;
    @JsonAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime startTime;

    public Task(String title, String description, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime endTime() {
        if (startTime != null && duration != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    @Override
    public String toString() {
        return id + ". " + title + "  " + description + " " + status + " " + duration + " " + startTime;
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
        return Objects.hash(id, title, description, duration, startTime);
    }

}

