package com.model.tasks;
public class Subtask extends Task {

    private final int epicId;

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return getId() + ". " + getTitle() + "  " + getDescription() + " " + getStatus();
    }
}
