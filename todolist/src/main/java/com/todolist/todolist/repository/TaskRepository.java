package com.todolist.todolist.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todolist.todolist.model.Task;

public interface TaskRepository extends JpaRepository<Task, UUID> {

	List<Task> findByIdUser(UUID idUser);
}
