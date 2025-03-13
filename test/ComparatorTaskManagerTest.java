import com.main.structure.taskmanager.ComparatorTaskManager;

import com.model.tasks.Task;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;


import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

public class ComparatorTaskManagerTest {
    private ComparatorTaskManager taskManager;


    @BeforeEach
    public void setUp() throws IOException {
        taskManager = new ComparatorTaskManager();

    }

    @Test
    public void addTaskTest() {
        taskManager = new ComparatorTaskManager();
        Task task1 = new Task("Task 1", "Description 1", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Task task2 = new Task("Task 2", "Description 2", Duration.ofHours(1), LocalDateTime.of(2023, 11, 1, 10, 0));
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Set<Task> tasks = taskManager.getPrioritizedTasks();

        assertEquals(2, tasks.size(), "Должно быть 2 задачи в tasks.");
    }

    @Test
    public void testAddTask_OverlapThrowsException() {
        taskManager = new ComparatorTaskManager();
        Task task1 = new Task("Task 1", "Description 1", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Task task2 = new Task("Task 2", "Description 2", Duration.ofHours(1), LocalDateTime.of(2023, 11, 1, 10, 0));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> taskManager.addTask(task2));

        assertEquals("Задача пересекается с другой задачей.", exception.getMessage());
    }

    @Test
    public void testGetPrioritizedTasks() {
        taskManager = new ComparatorTaskManager();
        Task task1 = new Task("Task 1", "Description 1", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Task task2 = new Task("Task 2", "Description 2", Duration.ofHours(1), LocalDateTime.of(2023, 11, 1, 10, 0));

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Set<Task> tasks = taskManager.getPrioritizedTasks();

        assertNotNull(tasks, "tasks не должно быть ноль.");
        assertTrue(tasks.contains(task1), "В списке должна  быть задача Task 1.");
        assertTrue(tasks.contains(task2), "В списке должна  быть задача Task 2.");
    }

    @Test
    public void testAddTask_NullStartTime() {
        taskManager = new ComparatorTaskManager();
        Task task1 = new Task("Task 1", "Description 1", Duration.ofHours(1), null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> taskManager.addTask(task1));

        assertNotNull(exception);
    }
}
