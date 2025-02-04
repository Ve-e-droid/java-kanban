package com.manager.taskManager;

import com.clases.Tasks.Epic;
import com.clases.Tasks.Subtask;
import com.clases.Tasks.Task;

import java.util.List;

public interface TaskManager {

    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubtask(Subtask subtask);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    List<Subtask> getSubtasksByEpic(int epicId);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    void deleteTaskByID(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

}
