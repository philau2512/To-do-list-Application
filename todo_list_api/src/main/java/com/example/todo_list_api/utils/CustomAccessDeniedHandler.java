package com.example.todo_list_api.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        String uri = request.getRequestURI();

        if (uri.startsWith("/api")) {
            // Nếu là API thì trả JSON 403
            response.setCharacterEncoding("UTF-8"); // ✅ quan trọng
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"message\": \"Bạn không có quyền truy cập tài nguyên này.\"}");
        } else {
            // Nếu là web thì redirect về trang /403
            response.sendRedirect("/403");
        }
    }
}

