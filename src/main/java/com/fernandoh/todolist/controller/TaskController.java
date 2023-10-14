package com.fernandoh.todolist.controller;

import com.fernandoh.todolist.model.Task;
import com.fernandoh.todolist.repository.TaskRepository;
import com.fernandoh.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Task task, HttpServletRequest request) throws Exception {
        Long userId = (Long) request.getAttribute("userId");
        task.setUserId(userId);

        LocalDateTime currentDate = LocalDateTime.now();
        if (currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getEndAt()))
            throw new Exception("Data de inicio / data de termino inválida");

        if (task.getStartAt().isAfter(task.getEndAt()))
            throw new Exception("Data de inicio deve ser menor que a data de término");

        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<Task>> listUserTasks(HttpServletRequest request) {
        List<Task> tasks = taskRepository.findByUserId((Long) request.getAttribute("userId"));
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Task newTask, @PathVariable Long id, HttpServletRequest request) throws Exception {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new Exception("Task não encontrada"));

        Long userId = (Long) request.getAttribute("userId");
        if (!task.getUserId().equals(userId))
            throw new Exception("Task não pertence ao usuário");

        Utils.copyNonNullProperties(newTask, task);
        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }
}
