package com.model.tasks;

public enum TaskType {
    TASK,
    EPIC,
    SUBTASK;

    public enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }
}
