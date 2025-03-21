package com.model.tasks;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds;

    public Epic(String title, String description, Duration duration, LocalDateTime startTime) {
        super(title, description, duration, startTime);
        this.subtaskIds = new ArrayList<>();

    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(int id) {
        subtaskIds.remove(Integer.valueOf(id));
    }

    @Override
    public LocalDateTime getStartTime() {
        return super.getStartTime();
    }

    @Override
    public Duration getDuration() {
        return super.getDuration();
    }

    public LocalDateTime getEndTime() {
        return (getStartTime() != null && getDuration() != null) ? getStartTime().plus(getDuration()) : null;
    }

    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
    }

    @Override
    public String toString() {
        return getId() + ". " + getTitle() + " " + getDescription() + " " + getStatus() + " " + getDuration() + " " + getStartTime();
    }

    public void setStatus(TaskType.Status status) {
        super.setStatus(status);
    }
}