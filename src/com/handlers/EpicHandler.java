package com.handlers;

import com.google.gson.JsonSyntaxException;
import com.http.BaseHttpHandler;
import com.google.gson.Gson;
import com.main.structure.taskmanager.InMemoryTaskManager;

import com.model.tasks.Epic;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class EpicHandler extends BaseHttpHandler {
    private final InMemoryTaskManager taskManager;
    private final Gson gson;

    public EpicHandler(InMemoryTaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET":
                    handleGetEpic(exchange);
                    break;
                case "POST":
                    handlePostEpic(exchange);
                    break;
                case "DELETE":
                    handleDeleteEpic(exchange);
                    break;
                default:
                    System.out.println("Метода " + exchange + " нет в списке.");
                    break;
            }

        } catch (IOException e) {
            exchange.sendResponseHeaders(500, -1);

        }
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String acceptHeader = exchange.getRequestHeaders().getFirst("Accept");
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] urlParts = path.split("/");

        if (acceptHeader == null || !acceptHeader.equals("application/json")) {
            sendHasInteractions(exchange);
            return;
        }

        if (urlParts.length == 3) {
            try {
                int id = Integer.parseInt(urlParts[2]);
                Epic epic = taskManager.getEpicById(id);

                if (epic == null) {

                    System.out.println("Epic not found for ID: " + id);

                    sendNotFound(exchange);
                    return;
                }
                taskManager.deleteEpicById(epic.getId());
                sendText(exchange, gson.toJson("Epic with ID " + id + " deleted"), 200);

            } catch (NumberFormatException e) {
                sendText(exchange, "Invalid epic ID", 400);
            }
        } else {

            sendNotFound(exchange);
        }
    }

    private void handleGetEpic(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] urlParts = path.split("/");

        if (urlParts.length == 2) {
            List<Epic> epics = taskManager.getAllEpics();
            String jsonResponse = new Gson().toJson(epics);
            System.out.println(jsonResponse);
            sendText(exchange, jsonResponse, 200);
        }

        if (urlParts.length == 3) {
            int id = Integer.parseInt(urlParts[2]);
            Epic epic = taskManager.getEpicById(id);
            String jsonResponse = new Gson().toJson(epic);
            if (epic != null) {
                sendText(exchange, jsonResponse, 200);
            } else {
                sendNotFound(exchange);
            }
        }
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {

        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            Epic epic = gson.fromJson(requestBody, Epic.class);
            taskManager.createEpic(epic);
            sendText(exchange, gson.toJson(epic), 201);
        } catch (JsonSyntaxException e) {

            sendText(exchange, "{\"error\":\"Invalid JSON\"}", 400);
        }
    }
}
