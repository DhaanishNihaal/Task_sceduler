package com.scheduler.taskscheduler.util;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CycleDetector {

    public static boolean hasCycle(Map<Integer, List<Integer>> graph) {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recursionStack = new HashSet<>();
        
        for (int node : graph.keySet()) {
            if (!visited.contains(node)) {
                if (dfs(node, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private static boolean dfs(int node, Map<Integer, List<Integer>> graph, 
                               Set<Integer> visited, Set<Integer> recursionStack) {
        visited.add(node);
        recursionStack.add(node);
        
        List<Integer> neighbors = graph.get(node);
        if (neighbors != null) {
            for (int neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    if (dfs(neighbor, graph, visited, recursionStack)) {
                        return true;
                    }
                } else if (recursionStack.contains(neighbor)) {
                    return true;  // Back edge found - cycle detected
                }
            }
        }
        
        recursionStack.remove(node);
        return false;
    }
}
