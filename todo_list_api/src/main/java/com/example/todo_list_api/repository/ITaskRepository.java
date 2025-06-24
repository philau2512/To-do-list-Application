package com.example.todo_list_api.repository;

import com.example.todo_list_api.model.Task;
import com.example.todo_list_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    List<Task> findByUserAndStatus(User user, String status);
}
