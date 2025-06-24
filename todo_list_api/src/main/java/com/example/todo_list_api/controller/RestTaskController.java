package com.example.todo_list_api.controller;

import com.example.todo_list_api.config.ApiResponse;
import com.example.todo_list_api.dto.TaskDto;
import com.example.todo_list_api.model.Task;
import com.example.todo_list_api.service.ITaskService;
import com.example.todo_list_api.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class RestTaskController {
    @Autowired
    private ITaskService taskService;

    @Autowired
    private IUserService userService;

    // --- get list task by user ---
    @GetMapping
    public ResponseEntity<?> listTasks() {
        List<TaskDto> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // --- get task by id ---
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        TaskDto task = taskService.getTaskById(id);
        if (task == null) {
            return new ResponseEntity<>(new ApiResponse("id task ko ton tai"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    // --- create task ---
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto) {
        taskService.createTask(taskDto);
        return new ResponseEntity<>(taskDto, HttpStatus.CREATED);
    }

    // --- update task ---
    @PatchMapping("/{id}/update")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        taskService.updateTask(id, taskDto);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }

    // --- delete task ---
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        TaskDto taskDto = taskService.getTaskById(id);
        if (taskDto == null) {
            return new ResponseEntity<>(new ApiResponse("id task ko ton tai"), HttpStatus.NOT_FOUND);
        }
        taskService.deleteTask(id);
        return new ResponseEntity<>(new ApiResponse("Delete Task thanh cong!"), HttpStatus.OK);
    }

    // --- toggle task status ---
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<?> toggleTaskStatus(@PathVariable Long id) {
        taskService.toggleTaskStatus(id);
        TaskDto taskDto = taskService.getTaskById(id);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }
}
