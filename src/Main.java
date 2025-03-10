import com.main.structure.taskmanager.FileBackedTaskManager;
import com.model.tasks.Epic;
import com.model.tasks.Subtask;
import com.model.tasks.Task;

import java.io.File;

    public class Main {
        public static void main(String[] args) {
            // Указываем имя файла для хранения задач
            File dataFile = new File("data.csv");

            // Создаем экземпляр нашего менеджера задач
            FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(dataFile);

            // Создаем задачи
            Task task1 = new Task("Сделать домашку", "Выполнить задачи по математике");
            Task task2 = new Task("Купить продукты", "Купить молоко и хлеб");

            // Создаем эпик
            Epic epic1 = new Epic("Проект по истории", "Подготовка к защите проекта");

            // Создаем подзадачи
            Subtask subtask1 = new Subtask("Написать отчет", "Написать отчет по проекту", epic1.getId());

            // Добавляем задачи в менеджер
            taskManager.createTask(task1);
            taskManager.createTask(task2);

            taskManager.createEpic(epic1);

            taskManager.createSubtask(subtask1);


            // Получаем и выводим все задачи
            System.out.println("Все задачи:");
            for (Task task : taskManager.getAllTasks()) {
                System.out.println(task);
            }
            for (Epic epic : taskManager.getAllEpics()) {
                System.out.println(epic);
            }

            for (Subtask subtask : taskManager.getAllSubtasks()){
                System.out.println(subtask);
            }

        }
    }