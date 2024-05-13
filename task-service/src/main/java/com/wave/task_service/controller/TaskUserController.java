package com.wave.task_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wave.task_service.Component.GetClaims;
import com.wave.task_service.Component.ValidateToken;
import com.wave.task_service.dto.TaskDto;
import com.wave.task_service.dto.TaskIdDto;
import com.wave.task_service.dto.UserDto;
import com.wave.task_service.service.TaskService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;



@RestController
@RequestMapping("/user-task-controller")
@RequiredArgsConstructor
@Tag(name = "Работа юзера с task entity")
public class TaskUserController 
{
    private final TaskService taskService;

    @GetMapping("/get-all")
    public Iterable<TaskDto> getAll(@RequestHeader(name = "Authorization") String token, @RequestParam String username) throws IOException 
    {
        if(!(new ValidateToken().validate(token.replaceFirst("Bearer ", ""))))
        {
            return null;
        }

        return taskService.getAllUserTasks(username);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTask(@RequestHeader(name = "Authorization") String token, @RequestBody TaskDto taskDto) throws IOException 
    {
        if(!(new ValidateToken().validate(token.replaceFirst("Bearer ", ""))))
        {
            return null;
        }
        taskService.add(taskDto);
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteTask(@RequestHeader(name = "Authorization") String token, @RequestBody TaskIdDto id) throws IOException
    {
        if(!(new ValidateToken().validate(token.replaceFirst("Bearer ", ""))))
        {
            return null;
        }
        
        taskService.deleteTask(id.getId());

        return ResponseEntity.ok().build();
    }
}
