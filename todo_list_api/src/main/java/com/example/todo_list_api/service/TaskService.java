package com.example.todo_list_api.service;

import com.example.todo_list_api.dto.TaskDto;
import com.example.todo_list_api.model.Task;
import com.example.todo_list_api.model.User;
import com.example.todo_list_api.repository.ITaskRepository;
import com.example.todo_list_api.repository.IUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService implements ITaskService {
    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private IUserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow();
    }

    @Override
    public List<TaskDto> getAllTasks() {
        List<TaskDto> listTaskDto = new ArrayList<>();
        List<Task> tasks = taskRepository.findByUser(getCurrentUser());
        for (Task task : tasks) {
            TaskDto taskDto = new TaskDto();
            BeanUtils.copyProperties(task, taskDto);
            listTaskDto.add(taskDto);
        }
        return listTaskDto;
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return null;
        }

        if (!task.getUser().equals(getCurrentUser())) {
            throw new AccessDeniedException("Không được truy cập task của người khác.");
        }
        TaskDto taskDto = new TaskDto();
        BeanUtils.copyProperties(task, taskDto);
        return taskDto;
    }

    @Override
    public void createTask(TaskDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus("todo");
        task.setUser(getCurrentUser());
        taskRepository.save(task);
    }

    @Override
    public void updateTask(Long id, TaskDto dto) {
        Task task = taskRepository.findById(id).orElseThrow();
        if (!task.getUser().equals(getCurrentUser())) {
            throw new AccessDeniedException("Không được sửa task của người khác.");
        }
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElse(null);

        if (!task.getUser().equals(getCurrentUser())) {
            throw new AccessDeniedException("Không được xóa task của người khác.");
        }
        taskRepository.delete(task);
    }

    @Override
    public void toggleTaskStatus(Long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        if (!task.getUser().equals(getCurrentUser())) {
            throw new AccessDeniedException("Không được thay đổi task của người khác.");
        }
        switch (task.getStatus()) {
            case "todo" -> task.setStatus("in progress");
            case "in progress" -> task.setStatus("done");
            case "done" -> task.setStatus("todo");
            default -> task.setStatus("todo");
        }
        taskRepository.save(task);
    }
}
