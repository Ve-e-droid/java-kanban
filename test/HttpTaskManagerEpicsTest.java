import com.adapter.DurationAdapter;
import com.adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.http.HttpTaskServer;
import com.main.structure.taskmanager.InMemoryTaskManager;
import com.managers.Managers;
import com.managers.TaskManager;
import com.model.tasks.Epic;
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


public class HttpTaskManagerEpicsTest {

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
    public void testAddEpic() {

        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("0", "Тест Epic", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();

        String taskJson = gson.toJson(epic);


        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();


        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            Assertions.assertEquals(201, response.statusCode());

            assertNotNull(manager.getAllEpics(), "Задачи не возвращаются");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testHandleGetEpicById() {

        Epic epic = new Epic("0", "Тест Epic", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        TaskManager manager = Managers.getDefault();
        manager.createEpic(epic);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        URI url = URI.create("http://localhost:8080/epics/1"); // Используйте http
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode());
            Assertions.assertEquals("0", manager.getEpicById(1).getTitle());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testHandleGetAllEpics() {
        Epic epic = new Epic("0", "Тест Epic", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Epic epic2 = new Epic("0", "Тест Epic", Duration.ofHours(1), LocalDateTime.of(2023, 10, 2, 10, 0));
        TaskManager manager = Managers.getDefault();
        manager.createEpic(epic);
        manager.createEpic(epic2);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        URI url = URI.create("http://localhost:8080/epics"); // Используйте http
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode());
            Assertions.assertEquals(2, manager.getAllEpics().size());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testHandleDeleteEpicsById() {
        Epic epic = new Epic("1", "Тест Epic", Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Epic epic2 = new Epic("2", "Тест Epic2", Duration.ofHours(1), LocalDateTime.of(2023, 11, 2, 10, 0));
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createEpic(epic);
        manager.createEpic(epic2);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode());
            manager.deleteEpicById(1);
            System.out.println(manager.getAllEpics());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}



