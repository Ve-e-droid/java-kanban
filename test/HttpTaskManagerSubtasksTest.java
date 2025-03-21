import com.adapter.DurationAdapter;
import com.adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.http.HttpTaskServer;
import com.managers.Managers;
import com.managers.TaskManager;
import com.model.tasks.Epic;
import com.model.tasks.Subtask;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerSubtasksTest {

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
    public void testAddSubtask() {
        Epic epic = new Epic("0", "Тест Epic", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        TaskManager manager = Managers.getDefault();
        manager.createEpic(epic);
        Subtask subtask = new Subtask("0", "Тест Subtask", epic.getId(), Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();

        String taskJson = gson.toJson(subtask);


        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();


        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            Assertions.assertEquals(201, response.statusCode());

            assertNotNull(manager.getAllSubtasks(), "Задачи не возвращаются");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testHandleGetSubtaskById() {

        Epic epic = new Epic("Title epic", "Тест Epic", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        TaskManager manager = Managers.getDefault();
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Title subtask", "Тест Subtask", epic.getId(), Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        manager.createSubtask(subtask);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode());
            Assertions.assertEquals("Title subtask", manager.getSubtaskById(2).getTitle());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testHandleGetAllSubtasks() {

        Epic epic = new Epic("Title epic", "Тест Epic", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        TaskManager manager = Managers.getDefault();
        manager.createEpic(epic);
        Subtask subtask = new Subtask("0", "Тест Subtask", epic.getId(), Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Subtask subtask2 = new Subtask("1", "Тест Subtask", epic.getId(), Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 12, 0));
        manager.createSubtask(subtask);
        manager.createSubtask(subtask2);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode());
            Assertions.assertEquals(2, manager.getAllSubtasks().size());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testHandleDeleteSubtasksById() {

        Epic epic = new Epic("Title epic", "Тест Epic", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        TaskManager manager = Managers.getDefault();
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Title subtask", "Тест Subtask", epic.getId(), Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        manager.createSubtask(subtask);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode());
            Assertions.assertNull(manager.getSubtaskById(1));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
