package com.handlers;

import com.google.gson.Gson;
import com.http.BaseHttpHandler;
import com.main.structure.historymanager.InMemoryHistoryManager;
import com.main.structure.taskmanager.InMemoryTaskManager;
import com.model.tasks.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;


public class HistoryHandler extends BaseHttpHandler {

    private InMemoryTaskManager taskManager;
    private final InMemoryHistoryManager historyManager;
    private final Gson gson;

    public HistoryHandler(InMemoryTaskManager taskManager, InMemoryHistoryManager historyManager, Gson gson) {
        this.taskManager = taskManager;
        this.historyManager = historyManager;
        this.gson = gson;

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            handleGetHistory(exchange);
        } else {
            sendNotFound(exchange);
        }


    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        String requestURI = exchange.getRequestURI().toString();

        if (requestURI.equals("/history")) {

            List<Task> history = historyManager.getHistory();

            sendText(exchange, gson.toJson(history), 200);

        } else {
            sendNotFound(exchange);
        }
    }


}