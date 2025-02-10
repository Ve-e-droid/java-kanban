package com.managers;
import com.main.structure.historymanager.HistoryManager;
import com.main.structure.historymanager.InMemoryHistoryManager;
import com.main.structure.taskmanager.InMemoryTaskManager;
import com.main.structure.taskmanager.TaskManager;

public class Managers {

    public static TaskManager getDefault() {

        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {

        return new InMemoryHistoryManager();
    }
}

