import com.clases.Tasks.Task;
import com.manager.historyManager.InMemoryHistoryManager;
import com.manager.taskManager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

        Task task = new Task("Задача 1", "описание 1");

        taskManager.createTask(task);
        taskManager.getTaskById(task.getId());

        assertEquals(task, taskManager.historyManager.getHistory().getFirst() , "Задачи должны быть одинаковыми.");
    }

    @Test
    void tasksHistoryDelete() {
        historyManager.addH(new Task("Task " + 1, "Description " + 1));
       Task deleteTask = historyManager.remove(0);

        Assertions.assertNull(deleteTask, "Удаленная задача должна возвращать null");

    }

    @Test
    void  linkLastTest() {
        Task task = new Task("Задача 1", "описание 1");
        Task task2 = new Task("Задача 2", "описание 2");
        Task task3 = new Task("Задача 3", "описание 3");

        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());

        assertEquals(task3, taskManager.historyManager.getHistory().getLast() , "Задача 3 должна быть последней.");

    }

    @Test
    void removeContainsKey() {
        Task task = new Task("Задача 1", "описание 1");
        Task task2 = new Task("Задача 2", "описание 2");
        Task task3 = new Task("Задача 3", "описание 3");

        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
        taskManager.getTaskById(task.getId());

        assertEquals(3, taskManager.historyManager.getHistory().size() , "Задача 3 должна быть последней.");
    }
}


