package com.manager.historyManager;

import com.clases.Tasks.Task;

import java.util.List;

public class Node {
    Task task;
    Node prev;
    Node next;

    public Node(Task task) {
        this.task = task;
    }
}
