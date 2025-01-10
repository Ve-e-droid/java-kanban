import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    ArrayList<Task> history = new ArrayList<>();

    @Override
    public void addHistory(Task task) {
        history.add(task);
        if (history.size() > 10) {
            history.removeFirst(); // Удаляем первый элемент
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}


