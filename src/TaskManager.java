import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TaskManager {

    private int nextId = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();


    public Task createTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(Epic epic){
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSubtask(Subtask subtask){
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic);
        }
        return subtask;
    }

    public List<Task> getAllTasks(){
        return new ArrayList<>(tasks.values());
    }

    public  List<Epic> getAllEpics(){
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubtasks(){
        return new ArrayList<>(subtasks.values());
    }

    public List<Subtask> getSubtasksByEpic(int epicId){ //метод возвращающий все подзадачи эпика по идентификатору эпика
        Epic epic = epics.get(epicId);
        if (epic != null) {
            List<Subtask> subtasksList = new ArrayList<>();
            for (Integer subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    subtasksList.add(subtask);
                }
            }
            return subtasksList;
        }
        return new ArrayList<>();
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);

    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic);
        }
    }

    private void updateEpic( Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public Task getTaskById(int id){
        return tasks.get(id);
    }

    public Epic getEpicById(int id){
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id){
        return subtasks.get(id);
    }

    public void deleteTasks() {
        tasks.clear();

    }

    public void deleteEpics(){
        epics.clear();

    }

    public void deleteSubtasks(){
        subtasks.clear();
    }

    public void deleteTaskByID(int id){
        tasks.remove(id);

    }

    public void deleteEpicById(int id){
        Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
    }

    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic);
            }
        }

    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();


        boolean allDone = true;
        boolean anyInProgress = false;

        for (Integer id : subtaskIds) {
            Subtask subtask = subtasks.get(id);
            if (subtask != null) {
                if (subtask.getStatus() == Status.IN_PROGRESS) {
                    anyInProgress = true;
                    allDone = false;
                    break;
                } else if (subtask.getStatus() == Status.NEW) {
                    allDone = false;
                }
            }
        }

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (anyInProgress) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
    }






}

