package com.example.todo_list_api.service;

import com.example.todo_list_api.dto.UserDto;
import com.example.todo_list_api.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IUserService {
    void registerUser(UserDto dto);
    UserDto getCurrentUserDto();
    User getCurrentUser(Authentication authentication);
    List<User> getAllUsers();
    UserDetails loadUserByUsername(String username);
}
