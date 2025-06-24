package com.example.todo_list_api.controller;

import com.example.todo_list_api.dto.TaskDto;
import com.example.todo_list_api.service.ITaskService;
import com.example.todo_list_api.service.IUserService;
import com.example.todo_list_api.service.TaskService;
import com.example.todo_list_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "tasks/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("task", new TaskDto());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute("task") TaskDto taskDto) {
        taskService.createTask(taskDto);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        return "tasks/edit";
    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute TaskDto taskDto) {
        taskService.updateTask(id, taskDto);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    @GetMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable Long id) {
        taskService.toggleTaskStatus(id);
        return "redirect:/tasks";
    }
}


