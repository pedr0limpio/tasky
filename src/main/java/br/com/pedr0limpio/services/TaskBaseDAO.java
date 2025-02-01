package br.com.pedr0limpio.services;

import br.com.pedr0limpio.models.Task;

import java.util.List;

public abstract class TaskBaseDAO {
    public abstract void writeTask(Task task);
    public abstract List<Task> getAllTasks();
    public abstract Task getById(int id);
    public abstract void update(int idFrom, Task taskFor);
    public abstract void removeById(int id);
}

