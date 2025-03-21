import com.adapter.DurationAdapter;
import com.adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.http.HttpTaskServer;
import com.managers.Managers;
import com.managers.TaskManager;
import com.model.tasks.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {
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
    public void testAddTask() {

        TaskManager manager = Managers.getDefault();
        Task task = new Task("Test Task", "Description of test task", Duration.ofMinutes(5), LocalDateTime.now());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();

        String taskJson = gson.toJson(task);


        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();


        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            Assertions.assertEquals(201, response.statusCode());

            assertNotNull(manager.getAllTasks(), "Задачи не возвращаются");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleGetAllTest() {
        Task task = new Task("Test Task", "Description of test task", Duration.ofMinutes(5), LocalDateTime.now());
        Task task1 = new Task("Test Task1", "Description of test task", Duration.ofMinutes(20), LocalDateTime.now());
        Task task2 = new Task("Test Task2", "Description of test task", Duration.ofMinutes(30), LocalDateTime.now());
        TaskManager manager = Managers.getDefault();
        manager.createTask(task);
        manager.createTask(task1);
        manager.createTask(task2);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        URI url = URI.create("http://localhost:8080/tasks"); // Используйте http
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode());
            Assertions.assertEquals(3, manager.getAllTasks().size());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testHandleGetTaskById() {

        Task task = new Task("Test Task", "Description of test task", Duration.ofMinutes(5), LocalDateTime.now());
        TaskManager manager = Managers.getDefault();
        manager.createTask(task);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        URI url = URI.create("http://localhost:8080/tasks/1"); // Используйте http
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode());
            Assertions.assertEquals("Test Task", manager.getTaskById(1).getTitle());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testHandleDeleteTasks() {
        Task task = new Task("Test Task", "Description of test task", Duration.ofMinutes(5), LocalDateTime.now());
        TaskManager manager = Managers.getDefault();
        manager.createTask(task);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        URI url = URI.create("http://localhost:8080/tasks/1"); // Используйте http
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