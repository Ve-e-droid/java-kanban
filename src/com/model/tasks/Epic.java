package com.model.tasks;

import com.status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> subtaskIds;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String title, String description, Duration duration, LocalDateTime startTime) {
        super(title, description, Duration.ZERO, null);
        this.subtaskIds = new ArrayList<>();
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.endTime = null;
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

    public void recalculateDuration(List<Task> subtasks) {
        duration = Duration.ZERO;
        startTime = null;
        endTime = null;

        for (int subtaskId : subtaskIds) {
            Task subtask = subtasks.get(subtaskId);
            duration = duration.plus(subtask.getDuration());

            if (startTime == null || subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }

            if (endTime == null || subtask.getStartTime().plus(subtask.getDuration()).isAfter(endTime)) {
                endTime = subtask.getStartTime().plus(subtask.getDuration());
            }
        }
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public Duration getDuration() {

        return duration;
    }


    public LocalDateTime getEndTime() {
        return endTime;
    }


    @Override
    public String toString() {
        return getId() + ". " + getTitle() + "  " + getDescription() + " " + getStatus() + " " + getDuration() + " " + getStartTime();
    }


    public void setStatus(Status status) {

    }
}