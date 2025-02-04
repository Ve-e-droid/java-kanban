package com.manager.historyManager;

import com.clases.Tasks.Task;

import java.util.List;

public interface HistoryManager {

    void addH(Task task);

    Task remove(int id);

    List<Task> getHistory();

}