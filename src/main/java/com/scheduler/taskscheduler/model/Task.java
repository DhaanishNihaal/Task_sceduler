
package com.scheduler.taskscheduler.model;

import java.util.List;

public class Task {
    private int id;
    private int priority;
    private int executionTime;
    private List<Integer> dependencies;

    public Task(int id, int priority, int executionTime, List<Integer> dependencies) {
        this.id = id;
        this.priority = priority;
        this.executionTime = executionTime;
        this.dependencies = dependencies;
    }

    public int getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public List<Integer> getDependencies() {
        return dependencies;
    }
}
