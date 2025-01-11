import com.clases.Tasks.Task;
import com.manager.historyManager.InMemoryHistoryManager;
import com.manager.taskManager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        ArrayList<Task> history = new ArrayList<>();
        Task task = new Task("Задача 1", "описание 1");

        taskManager.createTask(task);
        taskManager.getTaskById(task.getId());
        history.add(taskManager.tasks.get(task.getId()));

        assertEquals(1, history.size(), "История должна содержать одну задачу");
        assertEquals(task, history.getFirst(), "Задачи должны быть одинаковыми.");
    }

    @Test
    void tasksHistoryDeleteMoreThan10() {
        for (int i = 1; i <= 12; i++) {
            historyManager.addHistory(new Task("Task " + i, "Description " + i));
        }

        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size(), "История должна содержать только 10 задач.");
        assertFalse(history.contains(new Task("Task 1", "Description 1")), "Первая задача должна быть удалена из истории.");
        assertTrue(history.contains(new Task("Task 12", "Description 12")), "Последняя задача должна быть в истории.");
    }
}


