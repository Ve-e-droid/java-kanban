import com.main.structure.taskmanager.InMemoryTaskManager;
import com.model.tasks.Epic;
import com.model.tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        // Создание эпопеи
        Epic epic = new Epic("Задача 1", "описание 1", Duration.ZERO, LocalDateTime.of(2023, 10, 1, 12, 0));
        // Рекомендуется использовать уникальный идентификатор для эпопеи
        taskManager.createEpic(epic);

        // Создание подзадачи
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 12, 0));
        taskManager.createSubtask(subtask1);
        epic.addSubtaskId(1); // Добавление подзадачи к эпопее

        // Пересчет длительности эпопеи


        // Вывод результатов
        System.out.println("Длительность эпопеи: " + epic.getDuration());
        System.out.println("Начальное время эпопеи: " + epic.getStartTime());
        System.out.println("Конечное время эпопеи: " + epic.getEndTime());
    }
}