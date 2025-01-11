package com.clases.Tasks;
import com.status.status.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> subtaskIds;

    public Epic( String title, String description) {
        super( title, description);
        this.subtaskIds = new ArrayList<>();

    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId){
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(int id) {
        subtaskIds.remove(Integer.valueOf(id));
    }

    @Override
    public String toString() {
        return getId() + ". " + getTitle() + "  " + getDescription() + " " + getStatus();
    }


    public void setStatus(Status status) {

    }
}