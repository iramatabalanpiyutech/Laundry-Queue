package Service.DataManager.Repo;

import java.util.List;

public interface LoadRepo<T> {
    void addEntry(T item);
    void updateEntry(T item);
    void updateEntryState(T item);
    void deleteEntry(int id);
    T findById(int id);
    List<T> findAll();
    List<T> findWasherQueue();
    List<T> findDryerQueue();
}