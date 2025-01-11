package com.managers.managers;

import com.manager.historyManager.HistoryManager;
import com.manager.historyManager.InMemoryHistoryManager;
import com.manager.taskManager.InMemoryTaskManager;
import com.manager.taskManager.TaskManager;

public class Managers {

    public static TaskManager getDefault(){

        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){

        return new InMemoryHistoryManager();
    }
}

