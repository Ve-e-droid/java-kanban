package com.http;

import com.adapter.DurationAdapter;
import com.adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handlers.EpicHandler;
import com.handlers.HistoryHandler;
import com.handlers.SubtaskHandler;
import com.handlers.TaskHandler;

import com.main.structure.historymanager.InMemoryHistoryManager;
import com.main.structure.taskmanager.InMemoryTaskManager;
import com.managers.Managers;
import com.managers.TaskManager;
import com.sun.net.httpserver.HttpServer;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private final HttpServer httpServer;

    public HttpTaskServer(int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost", port);
        this.httpServer = HttpServer.create(address, 0);

        TaskManager manager = Managers.getDefault();
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();

        httpServer.createContext("/tasks", new TaskHandler(manager, gson));
        httpServer.createContext("/epics", new EpicHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubtaskHandler(manager, gson));
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        httpServer.createContext("/history", new HistoryHandler(historyManager, gson));


    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на 8080 порту!");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен");
    }

    public static void main(String[] args) {
        try {
            HttpTaskServer server = new HttpTaskServer(8080);
            server.start();
        } catch (IOException e) {
            System.out.println("Ошибка при создании сервера.");
        }
    }
}