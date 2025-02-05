package com.managers.managers;
import com.manager.historymanager.HistoryManager;
import com.manager.historymanager.InMemoryHistoryManager;
import com.manager.taskmanager.InMemoryTaskManager;
import com.manager.taskmanager.TaskManager;

public class Managers {

    public static TaskManager getDefault() {

        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {

        return new InMemoryHistoryManager();
    }
}

