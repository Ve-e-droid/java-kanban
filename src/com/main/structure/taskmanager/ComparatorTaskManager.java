package com.main.structure.taskmanager;

import com.comparator.TaskComparator;
import com.model.tasks.Task;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

public class ComparatorTaskManager {
    private final Set<Task> tasks = new TreeSet<>(new TaskComparator());

    public void addTask(Task task) {
        if (isOverlapping(task)) {
            throw new IllegalArgumentException("Задача пересекается с другой задачей.");
        }
        if (task.getStartTime() == null) {
            throw new IllegalArgumentException("Время начала не может быть null.");
        }
        updateTask(task);
        tasks.add(task);
    }

    public Set<Task> getPrioritizedTasks() {
        return tasks;
    }

    private boolean isOverlapping(Task newTask) {
        return tasks.stream().anyMatch(existingTask -> isTimeOverlapping(existingTask, newTask));
    }

    private boolean isTimeOverlapping(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null || task1.getDuration() == null || task2.getDuration() == null) {
            return false;
        }

        LocalDateTime endTime1 = task1.getStartTime().plus(task1.getDuration());
        LocalDateTime endTime2 = task2.getStartTime().plus(task2.getDuration());

        return task1.getStartTime().isBefore(endTime2) && task2.getStartTime().isBefore(endTime1);
    }

    public void updateTask(Task task) {
        if (tasks.contains(task)) {
            tasks.remove(task);
            tasks.add(task);
        }


    }
}
