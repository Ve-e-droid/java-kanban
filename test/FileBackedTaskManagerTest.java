import com.main.structure.taskmanager.FileBackedTaskManager;
import com.model.tasks.Epic;
import com.model.tasks.Subtask;
import com.model.tasks.Task;
import org.junit.jupiter.api.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    private File tempFile;
    private FileBackedTaskManager taskManager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("task_manager_test", ".csv");
        taskManager = new FileBackedTaskManager(tempFile);
    }

    @Test
    void saveAndLoadEmptyFile() {
        taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(taskManager.getAllTasks().isEmpty());
        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
    }

    @Test
    void saveMultipleTasks() {
        Task task1 = new Task("Task 1", "Description 1", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Task task2 = new Task("Task 2", "Description 2", Duration.ofHours(1), LocalDateTime.of(2023, 11, 1, 10, 0));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        Epic epic = new Epic("Epic 1", "Description Epic 1", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description Subtask 1", epic.getId(), Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 13, 0));
        taskManager.createSubtask(subtask);

        taskManager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(2, loadedManager.getAllTasks().size());
        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(1, loadedManager.getAllSubtasks().size());
    }

    @Test
    void loadMultipleTasks() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("id,type,name,status,description,epic,PT1H,2023-10-01T10:00\n");
            writer.write("1,TASK,Task 1,NEW,Description 1,PT1H,2023-10-01T10:00\n");
            writer.write("2,TASK,Task 2,NEW,Description 2,PT1H,2023-10-01T10:00\n");
            writer.write("3,EPIC,Epic 1,NEW,Description Epic 1,PT1H,2023-10-01T10:00\n");
            writer.write("4,SUBTASK,Subtask 1,NEW,Description Subtask 1,PT1H,2023-10-01T10:00,3 \n");
        } catch (IOException e) {
            System.out.println("Не удалось записать в файл: " + e.getMessage());
        }

        taskManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(2, taskManager.getAllTasks().size());
        assertEquals(1, taskManager.getAllEpics().size());
        assertEquals(1, taskManager.getAllSubtasks().size());
    }

    @AfterEach
    void tearDown() {

        if (!tempFile.delete()) {
            System.out.println("Не удалось удалить файл: " + tempFile.getAbsolutePath());
        }
    }
}
