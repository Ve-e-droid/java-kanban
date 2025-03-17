package com.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.http.BaseHttpHandler;
import com.managers.TaskManager;
import com.model.tasks.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public TaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {

            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET":
                    handleGetTask(exchange);
                    break;
                case "POST":
                    handlePostTask(exchange);
                    break;
                case "DELETE":
                    handleDeleteTask(exchange);
                    break;
                default:
                    break;
            }

        } catch (IOException e) {
            exchange.sendResponseHeaders(500, -1);
        } finally {
            exchange.close();
        }
    }

    public void handleDeleteTask(HttpExchange exchange) throws IOException {

        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] urlParts = path.split("/");

        if (urlParts.length == 2) {
            if (!manager.getAllTasks().isEmpty()) {
                manager.deleteTasks();
                sendText(exchange, gson.toJson("All tasks deleted"), 200);
            }
        } else if (urlParts.length == 3) {

            String idString = urlParts[2];
            int id = Integer.parseInt(idString);
            manager.deleteTaskByID(id);
            sendText(exchange, gson.toJson("Task " + id + " deleted."), 200);

        } else {
            sendNotFound(exchange);
        }
    }

    private void handleGetTask(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();

        String[] urlParts = path.split("/");

        if (urlParts.length == 2) {
            List<Task> tasks = manager.getAllTasks();
            String jsonResponse = new Gson().toJson(tasks);
            sendText(exchange, jsonResponse, 200);
        } else if (urlParts.length == 3) {
            int id = Integer.parseInt(urlParts[2]);
            Task task = manager.getTaskById(id);
            String jsonResponse = new Gson().toJson(task);
            sendText(exchange, jsonResponse, 200);

        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        try {
            byte[] bodyBytes = exchange.getRequestBody().readAllBytes();
            String requestBody = new String(bodyBytes, StandardCharsets.UTF_8);

            Task task = gson.fromJson(requestBody, Task.class);
            manager.createTask(task);
            sendText(exchange, gson.toJson(task), 201);


        } catch (JsonSyntaxException e) {
            sendText(exchange, "Invalid JSON format", 400);
        } catch (Exception e) {
            sendText(exchange, "Internal Server Error", 500);
        }
    }

}
