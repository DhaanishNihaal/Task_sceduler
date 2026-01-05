package com.scheduler.taskscheduler.util;

import com.scheduler.taskscheduler.model.Task;
import java.util.*;

public class Scheduler {

    public static List<Integer> schedule(List<Task> tasks) {
        Map<Integer, Task> taskMap = new HashMap<>();
        Map<Integer, Integer> indegree = new HashMap<>();
        Map<Integer, List<Integer>> graph = new HashMap<>();
        
        // Build task map and initialize graph
        for (Task task : tasks) {
            taskMap.put(task.getId(), task);
            indegree.put(task.getId(), 0);
            graph.put(task.getId(), new ArrayList<>());
        }
        
        // Build graph and calculate indegrees
        for (Task task : tasks) {
            for (int depId : task.getDependencies()) {
                graph.get(depId).add(task.getId());
                indegree.put(task.getId(), indegree.get(task.getId()) + 1);
            }
        }
        
        // Priority queue: higher priority first
        PriorityQueue<Integer> queue = new PriorityQueue<>((a, b) -> {
            return Integer.compare(taskMap.get(b).getPriority(), 
                                   taskMap.get(a).getPriority());
        });
        
        // Add all tasks with indegree 0
        for (int taskId : indegree.keySet()) {
            if (indegree.get(taskId) == 0) {
                queue.offer(taskId);
            }
        }
        
        List<Integer> executionOrder = new ArrayList<>();
        
        // Kahn's algorithm with priority queue
        while (!queue.isEmpty()) {
            int currentTask = queue.poll();
            executionOrder.add(currentTask);
            
            // Reduce indegree of neighbors
            for (int neighbor : graph.get(currentTask)) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                
                // If indegree becomes 0, add to queue
                if (indegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        // Check if all tasks were scheduled (no cycle)
        if (executionOrder.size() != tasks.size()) {
            throw new IllegalStateException("Cycle detected in task dependencies");
        }
        
        return executionOrder;
    }
}
