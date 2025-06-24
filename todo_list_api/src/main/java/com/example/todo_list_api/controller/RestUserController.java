package com.example.todo_list_api.controller;

import com.example.todo_list_api.config.ApiResponse;
import com.example.todo_list_api.dto.TaskDto;
import com.example.todo_list_api.dto.UserDto;
import com.example.todo_list_api.model.User;
import com.example.todo_list_api.service.ITaskService;
import com.example.todo_list_api.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RestUserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Đăng ký tài khoản mới
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserDto userDto) {
        try {
            userService.registerUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Đăng ký thành công!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Lỗi đăng ký: " + e.getMessage()));
        }
    }

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody UserDto dto, HttpServletRequest request) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

            Authentication auth = authenticationManager.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(auth);
            // ⚠️ XÓA session cũ nếu có
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            // ✅ TẠO session mới, sinh JSESSIONID mới đúng sau khi login
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            return ResponseEntity.ok(new ApiResponse("Đăng nhập thành công!"));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Tên đăng nhập hoặc mật khẩu không đúng."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Lỗi đăng nhập: " + e.getMessage()));
        }
    }

    // Đăng xuất
    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();

        return ResponseEntity.ok(new ApiResponse("Đăng xuất thành công!"));
    }

    // Thông tin user hiện tại
    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        try {
            User user = userService.getCurrentUser(authentication);
            List<TaskDto> tasks = taskService.getAllTasks();

            Map<String, Object> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("role", user.getRole());
            data.put("tasks", tasks);

            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Lỗi lấy thông tin người dùng: " + e.getMessage()));
        }
    }

    // Danh sách user (chỉ ADMIN)
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Lỗi lấy danh sách người dùng: " + e.getMessage()));
        }
    }

    @GetMapping("/get-role")
    public String test(Authentication auth) {
        System.out.println("Authorities: " + auth.getAuthorities());
        return "" + auth.getAuthorities();
    }
}

