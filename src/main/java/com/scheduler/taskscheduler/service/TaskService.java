package com.scheduler.taskscheduler.service;

import com.scheduler.taskscheduler.model.Task;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Service
public class TaskService {
    private HashMap<Integer, Task> tasks;

    public TaskService() {
        this.tasks = new HashMap<>();
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }
}
