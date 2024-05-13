package com.wave.task_service.service;

import java.util.ArrayList;
import java.util.UUID;
import java.sql.Connection;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wave.task_service.repository.TaskRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.wave.task_service.dto.TaskDto;
import com.wave.task_service.entity.Task;

@Data
@Service
@RequiredArgsConstructor
public class TaskService 
{
    @Value("${spring.datasource.url}")
    private String url;

    private final TaskRepository taskRepository;

    public Iterable<TaskDto> getAllUserTasks(String username)
    {
        Iterable<Task> tasks = taskRepository.findByUsername(username);
        ArrayList<TaskDto> tasksDto = new ArrayList<TaskDto>();

        for (Task task : tasks) 
        {
            tasksDto.add(new TaskDto().setId(task.getId()).setTitle(task.getTitle()).setDescription(task.getDescription()).setDeadline(task.getDeadline()));
        }

        return tasksDto;
    }

    public void add(TaskDto taskDto)
    {
        Task task = new Task()
            .setId(UUID.randomUUID().toString())
            .setTitle(taskDto.getTitle())
            .setDescription(taskDto.getDescription())
            .setDeadline(taskDto.getDeadline())
            .setUsername(taskDto.getUsername())
            .setCreatedAt(LocalDateTime.now());

        taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(String id)
    {
        taskRepository.delete(id);
        taskRepository.flush();
    }
}
