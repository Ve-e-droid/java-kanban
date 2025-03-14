import com.comparator.TaskComparator;
import com.model.tasks.Task;
import org.junit.Test;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskComparatorTest {
    private final TaskComparator taskComparator = new TaskComparator();

    @Test
    public void testCompareWithDifferentStartTime() {
        Task task1 = new Task("Task 1", "Description 1", null, LocalDateTime.of(2023, 10, 1, 10, 0));
        Task task2 = new Task("Task 2", "Description 2", null, LocalDateTime.of(2023, 11, 1, 10, 0));
        int result = taskComparator.compare(task1, task2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareWithSameStartTime() {
        Task task1 = new Task("Task 1", "Description 1", null, LocalDateTime.of(2023, 10, 1, 10, 0));
        Task task2 = new Task("Task 2", "Description 2", null, LocalDateTime.of(2023, 10, 1, 10, 0));
        int result = taskComparator.compare(task1, task2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareTask1StartTimeIsNull() {
        Task task1 = new Task("Task 1", "Description 1", null, null);
        Task task2 = new Task("Task 2", "Description 2", null, LocalDateTime.of(2023, 10, 1, 10, 0));
        int result = taskComparator.compare(task1, task2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareTask2StartTimeIsNull() {
        Task task1 = new Task("Task 1", "Description 1", null, LocalDateTime.of(2023, 10, 1, 10, 0));
        Task task2 = new Task("Task 2", "Description 2", null, null);
        int result = taskComparator.compare(task1, task2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareBothTaskAreNull() {
        Task task1 = new Task("Task 1", "Description 1", null, null);
        Task task2 = new Task("Task 2", "Description 2", null, null);
        int result = taskComparator.compare(task1, task2);
        assertEquals(0, result);
    }
}
