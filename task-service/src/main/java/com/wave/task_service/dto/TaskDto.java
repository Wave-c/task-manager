package com.wave.task_service.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TaskDto 
{
    private String id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private String username;
}
