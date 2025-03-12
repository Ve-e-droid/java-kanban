import com.model.tasks.Task;
import com.main.structure.historymanager.InMemoryHistoryManager;
import com.main.structure.taskmanager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    private InMemoryTaskManager taskManager;
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void addTasksToHistory() {

        Task task = new Task("Задача 1", "описание 1", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));

        taskManager.createTask(task);
        taskManager.getTaskById(task.getId());

        assertEquals(task, taskManager.historyManager.getHistory().getFirst(), "Задачи должны быть одинаковыми.");
    }

    @Test
    void tasksHistoryDelete() {
        Task task = new Task("Задача 1", "описание 1", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Task task2 = new Task("Задача 2", "описание 2", Duration.ofHours(1), LocalDateTime.of(2023, 10, 2, 10, 0));
        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task2.getId());

        taskManager.historyManager.remove(task.getId());

        assertEquals(task2, taskManager.historyManager.getHistory().getFirst(), "Задача 2 должна быть первой.");

    }

    @Test
    void linkLastTest() {
        Task task = new Task("Задача 1", "описание 1", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Task task2 = new Task("Задача 2", "описание 2", Duration.ofHours(1), LocalDateTime.of(2023, 10, 2, 10, 0));
        Task task3 = new Task("Задача 3", "описание 3", Duration.ofHours(1), LocalDateTime.of(2023, 10, 3, 10, 0));

        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());

        assertEquals(task3, taskManager.historyManager.getHistory().getLast(), "Задача 3 должна быть последней.");

    }

    @Test
    void removeContainsKey() {
        Task task = new Task("Задача 1", "описание 1", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Task task2 = new Task("Задача 2", "описание 2", Duration.ofHours(1), LocalDateTime.of(2023, 10, 2, 10, 0));
        Task task3 = new Task("Задача 3", "описание 3", Duration.ofHours(1), LocalDateTime.of(2023, 10, 3, 10, 0));

        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
        taskManager.getTaskById(task.getId());

        assertEquals(3, taskManager.historyManager.getHistory().size(), "Задача 3 должна быть последней.");
    }
}


