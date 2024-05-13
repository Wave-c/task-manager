package com.wave.task_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wave.task_service.entity.Task;

import jakarta.transaction.Transactional;

@Transactional
@Repository
public interface TaskRepository extends JpaRepository<Task, String>
{
    Iterable<Task> findByUsername(String username);

    @Modifying
    @Query("delete from Task a WHERE a.id = :id")
    void delete(@Param("id") String id);
}