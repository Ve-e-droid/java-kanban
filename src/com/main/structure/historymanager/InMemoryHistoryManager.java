package com.main.structure.historymanager;

import com.managers.HistoryManager;
import com.model.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    Map<Integer, Node> historyMap = new HashMap<>();

    public void remove(int id) {
        Node remNode = historyMap.get(id);
        if (remNode != null) {
            removeNode(remNode);
        }


    }

    public void linkLast(Task task) {
        Node newNode = new Node(task);

        if (historyMap.containsKey(task.getId())) {
            removeNode(historyMap.get(task.getId()));
        }

        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        historyMap.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        System.out.println(tasks);
        return tasks;
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        historyMap.remove(node.task.getId());
    }


    @Override
    public void addHistory(Task task) {
        if (historyMap.containsKey(task.getId())) {

            removeNode(historyMap.get(task.getId()));
        }

        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

}