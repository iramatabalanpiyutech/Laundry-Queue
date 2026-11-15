package Service.DataManager.Repo;

import java.util.List;

public interface RepoSpecificallyForMachineAss<T> {
    void addEntry(T item);
    void updateEntry(T item);
    void updateStateStart(T item);
    void updateStateEnd(T item);
    void deleteEntry(int id);
    T findById(int id);
    List<T> findAll();
    boolean  hasActiveLoadAssignment(int id);
    boolean hasActiveMachineAssignment(int id);
}