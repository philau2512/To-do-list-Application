package com.example.todo_list_api.service;

import com.example.todo_list_api.dto.TaskDto;
import com.example.todo_list_api.model.Task;
import com.example.todo_list_api.model.User;

import java.util.List;

public interface ITaskService {
    List<TaskDto> getAllTasks(); // Lấy task của user hiện tại
    TaskDto getTaskById(Long id);
    void createTask(TaskDto dto);
    void updateTask(Long id, TaskDto dto);
    void deleteTask(Long id);
    void toggleTaskStatus(Long id); // chuyển trạng thái giữa todo → in progress → done
}

