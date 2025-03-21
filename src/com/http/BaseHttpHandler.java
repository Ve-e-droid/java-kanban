package com.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    protected void sendText(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange t) throws IOException {
        String response = "{\"error\":\"Not Found\"}";
        byte[] resp = response.getBytes(StandardCharsets.UTF_8);
        t.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        t.sendResponseHeaders(404, resp.length);
        t.getResponseBody().write(resp);
        t.close();
    }

    protected void sendHasInteractions(HttpExchange g) throws IOException {
        String response = "{\"error\":\"Not Acceptable\"}";
        byte[] resp = response.getBytes(StandardCharsets.UTF_8);
        g.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        g.sendResponseHeaders(406, resp.length);
        g.getResponseBody().write(resp);
        g.close();
    }
}