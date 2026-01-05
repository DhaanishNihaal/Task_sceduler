package com.scheduler.taskscheduler.util;

import com.scheduler.taskscheduler.model.Task;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class GraphBuilder {

    public static Map<Integer, List<Integer>> buildGraph(List<Task> tasks) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        
        // Initialize adjacency list for all tasks
        for (Task task : tasks) {
            graph.putIfAbsent(task.getId(), new ArrayList<>());
        }
        
        // Build edges: dependency → task
        for (Task task : tasks) {
            for (int dependencyId : task.getDependencies()) {
                graph.putIfAbsent(dependencyId, new ArrayList<>());
                graph.get(dependencyId).add(task.getId());
            }
        }
        
        return graph;
    }
}
