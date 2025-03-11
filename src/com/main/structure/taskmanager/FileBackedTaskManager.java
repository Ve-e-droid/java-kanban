package com.main.structure.taskmanager;

import com.managers.ManagerSaveException;
import com.model.tasks.Epic;
import com.model.tasks.Subtask;
import com.model.tasks.Task;
import com.status.TaskTypeStatus;

import java.io.*;

import static com.status.TaskTypeStatus.*;

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
            fileWriter.write("id,type,name,status,description,epic\n");
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
        return task.getId() + "," + TASK + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescription() + "," + "\n";
    }

    protected String epicToString(Epic epic) {
        return epic.getId() + "," + EPIC + "," + epic.getTitle() + "," + epic.getStatus() + "," + epic.getDescription() + "," + "\n";
    }

    protected String subtaskToString(Subtask subtask) {
        return subtask.getId() + "," + SUBTASK + "," + subtask.getTitle() + "," + subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getEpicId() + "\n";
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    int id = Integer.parseInt(parts[0]);
                    TaskTypeStatus type = TaskTypeStatus.valueOf(parts[1].trim());
                    String title = parts[2];
                    String status = parts[3];
                    String description = parts[4];

                    switch (type) {
                        case TASK:
                            Task task = new Task(title, description);
                            manager.createTask(task);
                            break;
                        case EPIC:
                            Epic epic = new Epic(title, description);
                            manager.createEpic(epic);
                            break;
                        case SUBTASK:
                            int epicId = Integer.parseInt(parts[5].trim());
                            Subtask subtask = new Subtask(title, description, epicId);
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
