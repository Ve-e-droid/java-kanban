import com.http.HttpTaskServer;
import com.main.structure.taskmanager.InMemoryTaskManager;
import com.model.tasks.Epic;
import com.model.tasks.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpHistoryManagerTest {

    private static HttpTaskServer server;
    private static final int PORT = 8080;

    @BeforeAll
    public static void startServer() throws IOException {
        server = new HttpTaskServer(PORT);
        server.start();
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    @Test
    public void getHistoryTest() throws IOException {

        Task task = new Task("Test Task", "Description of test task", Duration.ofMinutes(5), LocalDateTime.now());
        Epic epic = new Epic("0", "Тест Epic", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.historyManager.addHistory(task);
        taskManager.historyManager.addHistory(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/history");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(2, taskManager.historyManager.getHistory().size());
    }
}