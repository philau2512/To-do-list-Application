package com.example.todo_list_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;
    private String status; // "todo", "in progress", "done"

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
}
