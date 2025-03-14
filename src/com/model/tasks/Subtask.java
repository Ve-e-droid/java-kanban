package com.model.tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private final int epicId;

    public Subtask(String title, String description, int epicId, Duration duration, LocalDateTime startTime) {
        super(title, description, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return getId() + ". " + getTitle() + "  " + getDescription() + " " + getStatus() + " " + getDuration() + " " + getStartTime();
    }
}
