package com.manager.historyManager;
import com.clases.Tasks.Task;
import com.manager.taskManager.TaskManager;
import java.util.List;

public interface HistoryManager {

    void addHistory(Task task);

    List<Task> getHistory();

}