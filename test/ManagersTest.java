import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefaultTestInMemoryTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "TaskManager не должен быть равен null");
        assertInstanceOf(InMemoryTaskManager.class, taskManager, "Должен иметь InMemoryTaskManager");
    }

    @Test
    void getDefaultHistoryTestInMemoryHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager,"HistoryManager не должен быть равен null");
        assertInstanceOf(InMemoryHistoryManager.class, historyManager, "Должен иметь InMemoryHistoryManager");
    }
}