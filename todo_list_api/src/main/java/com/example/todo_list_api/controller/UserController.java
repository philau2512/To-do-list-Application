package com.example.todo_list_api.controller;

import com.example.todo_list_api.dto.TaskDto;
import com.example.todo_list_api.dto.UserDto;
import com.example.todo_list_api.model.User;
import com.example.todo_list_api.service.ITaskService;
import com.example.todo_list_api.service.IUserService;
import com.example.todo_list_api.service.TaskService;
import com.example.todo_list_api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private ITaskService taskService;

    @GetMapping("/403")
    public String accessDeniedPage() {
        return "403Page";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "registerPage";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserDto dto,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        try {
            userService.registerUser(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Hãy đăng nhập.");
            return "redirect:/login"; // ✅ sau khi đăng ký, về lại trang login
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "registerPage";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/userInfo")
    public String userInfo(Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        List<TaskDto> taskList = taskService.getAllTasks();

        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("role", currentUser.getRole());
        model.addAttribute("tasks", taskList);

        return "users/userInfo";
    }

}
