import com.model.tasks.Epic;
import com.model.tasks.Subtask;
import com.model.tasks.Task;
import com.main.structure.taskmanager.InMemoryTaskManager;
import com.model.tasks.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;
    Task task = new Task("0", "Тест Task", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
    Task task2 = new Task("1", "Тест Task", Duration.ofHours(1), LocalDateTime.of(2023, 10, 2, 10, 0));

    Epic epic = new Epic("0", "Тест Epic", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
    Epic epic2 = new Epic("0", "Тест Epic", Duration.ofHours(1), LocalDateTime.of(2023, 10, 2, 10, 0));

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();

    }

    @Test
    void testTasksAreEqualWhenIdIsSame() {
        task.setId(1);
        task2.setId(1);
        assertTrue(task.equalsFull(task2), "Задачи должны быть равны, если их идентификаторы равны");
    }

    @Test
    void testEpicsAreEqualWhenIdIsSame() {
        epic.setId(1);
        epic2.setId(1);
        assertTrue(epic.equalsFull(epic2), "Эпики должны быть равны, если их идентификаторы равны");
    }

    @Test
    void testSubtasksAreEqualWhenIdIsSame() {
        Subtask subtask = new Subtask("0", "Тест Subtask", epic.getId(), Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Subtask subtask2 = new Subtask("1", "Тест Subtask", epic.getId(), Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 12, 0));
        subtask.setId(1);
        subtask2.setId(1);
        assertTrue(subtask.equalsFull(subtask2), "Подзадачи должны быть равны, если их идентификаторы равны");
    }

    @Test
    void testEpicObjectCannotBeAddedToItself() {
        epic.setId(1);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("0", "Тест Subtask", epic.getId(), Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        subtask.setId(epic.getId());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> taskManager.createSubtask(subtask));

        String expectedMessage = "Нельзя добавить самого себя в качестве подзадачи.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createTaskTestAddTask() {
        Task createdTask = taskManager.createTask(task);
        Assertions.assertNotNull(createdTask);
        assertEquals(1, createdTask.getId());
        assertEquals(TaskType.Status.NEW, createdTask.getStatus());
    }

    @Test
    void createEpicTestAddEpic() {
        Epic createdEpic = taskManager.createEpic(epic);
        Assertions.assertNotNull(createdEpic);
        assertEquals(1, createdEpic.getId());
    }

    @Test
    void createSubtaskTestAddSubtaskAndUpdateEpicStatus() {
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("0", "Тест Subtask", epic.getId(), Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Subtask createdSubtask = taskManager.createSubtask(subtask);

        Assertions.assertNotNull(createdSubtask);
        assertEquals(2, createdSubtask.getId());
        assertEquals("Тест Subtask", createdSubtask.getDescription());


        Epic updatedEpic = taskManager.getEpicById(epic.getId());
        assertEquals(TaskType.Status.NEW, updatedEpic.getStatus());
    }

    @Test
    void getTaskByIdTestAddToHistory() {
        taskManager.createTask(task);

        taskManager.getTaskById(1);

        ArrayList<Task> tasks = (ArrayList<Task>) taskManager.historyManager.getHistory();

        assertEquals(1, tasks.size());
        assertEquals(task, tasks.getFirst());
    }

    @Test
    void getSubtaskByIdTestAddToHistory() {
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("0", "Тест Subtask", epic.getId(), Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        taskManager.createSubtask(subtask);

        int subtaskId = subtask.getId();
        taskManager.getSubtaskById(subtaskId);

        ArrayList<Task> history = (ArrayList<Task>) taskManager.historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(subtask, history.getFirst(), "History");
    }

    @Test
    void deleteTaskByIdTestRemoveTask() {
        taskManager.createTask(task);
        taskManager.deleteTaskByID(task.getId());

        Task deletedTask = taskManager.getTaskById(task.getId());
        Assertions.assertNull(deletedTask, "Удаленная задача должна возвращать null");
    }

    @Test
    void deleteEpicByIdTestRemoveEpicAndItsSubtasks() {
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("0", "Subtask в epic", epic.getId(), Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        taskManager.createSubtask(subtask);
        taskManager.deleteEpicById(epic.getId());
        Epic deleteEpic = taskManager.getEpicById(epic.getId());
        Assertions.assertNull(deleteEpic, "Удаленный эпик должен возвращать null");
        Subtask deleteSubtask = taskManager.getSubtaskById(subtask.getId());
        Assertions.assertNull(deleteSubtask, "Удаленная подзадача должна возвращать null");
    }

    @Test
    void deleteSubtaskByIdTestRemoveSubtaskFromEpic() {
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("0", "Тест Subtask", epic.getId(), Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        taskManager.createSubtask(subtask);
        taskManager.deleteSubtaskById(subtask.getId());
        List<Subtask> subtasks = taskManager.getSubtasksByEpic(epic.getId());
        assertTrue(subtasks.isEmpty());
    }

    @Test
    void testDeleteTasks() {
        taskManager.createTask(task);
        taskManager.deleteTasks();
        assertTrue(taskManager.tasks.isEmpty(), "Удаленная задача должна быть null");

    }

    @Test
    void testDeleteEpics() {
        taskManager.createEpic(epic);
        taskManager.deleteEpics();
        assertTrue(taskManager.epics.isEmpty(), "Удаленный эпик должен возвращать null");
    }

    @Test
    void testDeleteSubtasks() {
        Subtask subtask = new Subtask("0", "Тест Subtask", epic.getId(), Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        epic.setId(1);
        subtask.setId(2);
        taskManager.createSubtask(subtask);
        taskManager.deleteSubtasks();
        assertTrue(taskManager.subtasks.isEmpty(), "Удаленная подзадача должна быть null");
    }

    @Test
    void testGetAllTasks() {
        Task task = new Task("Задача 1", "описание 1", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Task task2 = new Task("Задача 2", "описание 2", Duration.ofHours(1), LocalDateTime.of(2023, 10, 2, 10, 0));
        Task task3 = new Task("Задача 3", "описание 3", Duration.ofHours(1), LocalDateTime.of(2023, 10, 3, 10, 0));

        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        assertEquals(3, taskManager.tasks.size(), "Список должен быть равен 3.");
    }

    @Test
    void testGetAllEpics() {
        Epic epic = new Epic("Задача 1", "описание 1", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Epic epic2 = new Epic("Задача 2", "описание 2", Duration.ofHours(1), LocalDateTime.of(2023, 10, 2, 10, 0));
        Epic epic3 = new Epic("Задача 3", "описание 3", Duration.ofHours(1), LocalDateTime.of(2023, 10, 3, 10, 0));

        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);

        assertEquals(3, taskManager.epics.size(), "Список должен быть равен 3.");

    }

    @Test
    void testGetAllSubtask() {
        Subtask subtask = new Subtask("1", "Тест Subtask", 1, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Subtask subtask2 = new Subtask("2", "Тест Subtask", 1, Duration.ofHours(1), LocalDateTime.of(2023, 10, 2, 10, 0));
        Subtask subtask3 = new Subtask("3", "Тест Subtask", 1, Duration.ofHours(1), LocalDateTime.of(2023, 10, 3, 10, 0));

        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        assertEquals(3, taskManager.subtasks.size(), "Список должен быть равен 3.");

    }

    @Test
    void testRecalculateDuration() {
        Epic epic3 = new Epic("0", "Тест Epic", Duration.ofHours(1), LocalDateTime.of(2023, 10, 2, 10, 0));
        taskManager.createEpic(epic3);
        Subtask subtask = new Subtask("0", "Subtask в epic", epic3.getId(), Duration.ofHours(1), LocalDateTime.of(2023, 10, 2, 10, 30));
        taskManager.createSubtask(subtask);

        assertEquals(Duration.ofHours(2), epic3.getDuration());

    }
}