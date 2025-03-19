package com.main.structure.taskmanager;

import com.managers.ManagerSaveException;
import com.model.tasks.Epic;
import com.model.tasks.Subtask;
import com.model.tasks.Task;
import com.model.tasks.TaskType;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

import static com.model.tasks.TaskType.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File data;

    public FileBackedTaskManager(File file) {
        this.data = file;
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        save();
        return createdTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        save();
        return createdEpic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask createdSubtask = super.createSubtask(subtask);
        save();
        return createdSubtask;
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(data)) {
            fileWriter.write("id,type,name,status,description,epic,duration,startTime,\n");
            for (Task task : getAllTasks()) {
                fileWriter.write(taskToString(task));
            }

            for (Epic epic : getAllEpics()) {
                fileWriter.write(epicToString(epic));
            }

            for (Subtask subtask : getAllSubtasks()) {
                fileWriter.write(subtaskToString(subtask));
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении данных в файл", e);
        }

    }

    protected static String taskToString(Task task) {
        String startTimeString = (task.getStartTime() != null) ? task.getStartTime().toString() : "null";
        return task.getId() + "," + TASK + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescription() + "," + task.getDuration() + "," + startTimeString + "," + "\n";
    }

    protected String epicToString(Epic epic) {
        String startTimeString = (epic.getStartTime() != null) ? epic.getStartTime().toString() : "null";
        return epic.getId() + "," + EPIC + "," + epic.getTitle() + "," + epic.getStatus() + "," + epic.getDescription() + "," + epic.getDuration() + "," + startTimeString + "," + "\n";
    }

    protected String subtaskToString(Subtask subtask) {
        String startTimeString = (subtask.getStartTime() != null) ? subtask.getStartTime().toString() : "null";
        return subtask.getId() + "," + SUBTASK + "," + subtask.getTitle() + "," + subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getDuration() + "," + startTimeString + "," + subtask.getEpicId() + "\n";
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    int id = Integer.parseInt(parts[0]);
                    TaskType type = TaskType.valueOf(parts[1].trim());
                    String title = parts[2];
                    String status = parts[3];
                    String description = parts[4];

                    String durationString = parts[5];
                    Duration duration = Duration.parse(durationString);

                    LocalDateTime startTime = null;
                    String startTimeString = parts[6].trim();
                    if (!startTimeString.equals("null") && !startTimeString.isEmpty()) {
                        startTime = LocalDateTime.parse(startTimeString);
                    }

                    switch (type) {
                        case TASK:
                            Task task = new Task(title, description, duration, startTime);
                            manager.createTask(task);
                            break;
                        case EPIC:
                            Epic epic = new Epic(title, description, duration, startTime);
                            manager.createEpic(epic);
                            break;
                        case SUBTASK:
                            int epicId = Integer.parseInt(parts[7].trim());
                            Subtask subtask = new Subtask(title, description, epicId, duration, startTime);
                            manager.createSubtask(subtask);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных из файла", e);
        }
        return manager;
    }


}

