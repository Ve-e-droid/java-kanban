package com.manager.historymanager;
import com.model.tasks.Task;

public class Node {
    Task task;
    Node prev;
    Node next;

    public Node(Task task) {
        this.task = task;
    }
}
