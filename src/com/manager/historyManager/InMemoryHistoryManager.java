package com.manager.historyManager;

import com.clases.Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    HashMap<Integer, Node> hisMap  = new HashMap<>();

   public Task remove(int id) {
       Node remNode = hisMap.get(id);
       if (remNode != null) {
            removeNode(remNode);
       }
       return null;
   }

   public void linkLast(Task task) {
       Node newNode = new Node(task);

       if (hisMap.containsKey(task.getId())) {
           removeNode(hisMap.get(task.getId()));
       }

       if (tail == null) {
           head = newNode;
           tail = newNode;
       } else {
           tail.next = newNode;
           newNode.prev = tail;
           tail = newNode;
       }
       hisMap.put(task.getId(), newNode);
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
        hisMap.remove(node.task.getId());
    }


    @Override
    public void addH(Task task) {
        if (hisMap.containsKey(task.getId())) {

            removeNode(hisMap.get(task.getId()));
        }

        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

}