package com.scheduler.taskscheduler.controller;

import com.scheduler.taskscheduler.model.Task;
import com.scheduler.taskscheduler.service.TaskService;
import com.scheduler.taskscheduler.util.CycleDetector;
import com.scheduler.taskscheduler.util.GraphBuilder;
import com.scheduler.taskscheduler.util.Scheduler;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public Task addTask(@RequestBody Task task) {
        taskService.addTask(task);
        return task;
    }

    @GetMapping("/schedule")
    public List<Integer> getSchedule() {
        List<Task> tasks = taskService.getAllTasks();
        
        Map<Integer, List<Integer>> graph = GraphBuilder.buildGraph(tasks);
        
        if (CycleDetector.hasCycle(graph)) {
            throw new IllegalStateException("Cycle detected in task dependencies");
        }
        
        return Scheduler.schedule(tasks);
    }
}
