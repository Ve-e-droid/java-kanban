package com.main.structure.taskmanager;

import com.managers.TaskManager;
import com.model.tasks.Epic;
import com.model.tasks.Subtask;
import com.model.tasks.Task;
import com.main.structure.historymanager.InMemoryHistoryManager;
import com.status.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int nextId = 1;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public InMemoryHistoryManager historyManager = new InMemoryHistoryManager();


    @Override
    public Task createTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {

        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask.getEpicId() == subtask.getId()) {
            throw new IllegalArgumentException("Нельзя добавить самого себя в качестве подзадачи.");

        }
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic);
            recalculateDuration(epic);
        }
        return subtask;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getSubtasksByEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            List<Subtask> subtasksList = new ArrayList<>();
            for (Integer subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    subtasksList.add(subtask);
                }
            }
            return subtasksList;
        }
        return new ArrayList<>();
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic);
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.addHistory(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.addHistory(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.addHistory(subtask);
        }
        return subtask;
    }

    @Override
    public void deleteTasks() {
        tasks.clear();

    }

    @Override
    public void deleteEpics() {
        epics.clear();

    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
    }

    @Override
    public void deleteTaskByID(int id) {
        tasks.remove(id);

    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic);
            }
        }

    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateEpicStatus(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();


        boolean allDone = true;
        boolean anyInProgress = false;

        for (Integer id : subtaskIds) {
            Subtask subtask = subtasks.get(id);
            if (subtask != null) {
                if (subtask.getStatus() == Status.IN_PROGRESS) {
                    anyInProgress = true;
                    allDone = false;
                    break;
                } else if (subtask.getStatus() == Status.NEW) {
                    allDone = false;
                }
            }
        }

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (anyInProgress) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
    }

    public void recalculateDuration(Epic epic) {
        Duration duration = Duration.ZERO;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        for (int subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);

            if (subtask != null) {
                duration = epic.getDuration().plus(subtask.getDuration());
                if (subtask.getStartTime() != null) {
                    if (startTime == null || subtask.getStartTime().isBefore(startTime)) {
                        startTime = subtask.getStartTime();

                    }

                } else {
                    System.out.println("subtask.getStartTime() равна null.");
                }
                LocalDateTime subtaskEndTime = subtask.getStartTime().plus(subtask.getDuration());

                if (endTime == null || subtaskEndTime.isAfter(endTime)) {
                    endTime = subtaskEndTime;
                }
            } else {
                System.out.println("Подзадача с ID " + subtaskId + " не найдена.");
            }

        }

        epic.setDuration(duration);
        epic.setStartTime(startTime);
        epic.endTime();

    }

}






