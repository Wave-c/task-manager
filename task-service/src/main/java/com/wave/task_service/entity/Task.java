package com.wave.task_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@Table(name = "task")
@Accessors(chain = true)
@NoArgsConstructor
public class Task 
{
    @Id
    private String id;

    private String title;
    private String description;
    private LocalDateTime deadline;
    private String username;
    private LocalDateTime createdAt;
}