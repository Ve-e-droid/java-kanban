package com.handlers;

import com.google.gson.JsonSyntaxException;
import com.http.BaseHttpHandler;
import com.google.gson.Gson;
import com.managers.TaskManager;
import com.model.tasks.Subtask;
import com.sun.net.httpserver.HttpExchange;


import java.io.IOException;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class SubtaskHandler extends BaseHttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public SubtaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    handleGetSubtask(exchange);
                    break;
                case "POST":
                    handlePostSubtask(exchange);
                    break;
                case "DELETE":
                    handleDeleteSubtask(exchange);
                    break;
                default:
                    sendNotFound(exchange);
                    break;
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(500, -1);
        } finally {
            exchange.close();
        }

    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        String requestURI = exchange.getRequestURI().toString();

        if (requestURI.startsWith("/subtasks/")) {

            String idString = requestURI.substring(requestURI.lastIndexOf("/") + 1);

            int id = Integer.parseInt(idString);
            if (!(manager.getSubtaskById(id) == null)) {
                manager.deleteSubtaskById(id);
                sendText(exchange, gson.toJson("Subtask deleted"), 200);
            } else {
                sendText(exchange, "{\"error\":\"Invalid ID\"}", 400);
            }
        } else {
            sendText(exchange, "{\"error\":\"Invalid requestURI\"}", 400);
        }
    }

    private void handleGetSubtask(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        System.out.println(path);
        String[] urlParts = path.split("/");

        if (urlParts.length == 2) {
            List<Subtask> subtasks = manager.getAllSubtasks();
            String jsonResponse = new Gson().toJson(subtasks);
            System.out.println(jsonResponse);
            sendText(exchange, jsonResponse, 200);
        }

        if (urlParts.length == 3) {
            int id = Integer.parseInt(urlParts[2]);
            Subtask subtask = manager.getSubtaskById(id);
            String jsonResponse = new Gson().toJson(subtask);
            if (subtask != null) {
                sendText(exchange, jsonResponse, 200);
            } else {
                sendNotFound(exchange);
            }
        }
    }

    private void handlePostSubtask(HttpExchange exchange) throws IOException {

        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            Subtask subtask = gson.fromJson(requestBody, Subtask.class);
            manager.createSubtask(subtask);
            sendText(exchange, gson.toJson(subtask), 201);
        } catch (JsonSyntaxException e) {

            sendText(exchange, "{\"error\":\"Invalid JSON\"}", 400);
        }
    }
}
