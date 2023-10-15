package com.todolist.todolist.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.todolist.model.Task;
import com.todolist.todolist.repository.TaskRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

	
	@Autowired
	private TaskRepository taksRepository;
	
	
	@PostMapping("/")
	public ResponseEntity<Task> create(@RequestBody Task task, HttpServletRequest request){
		// Restringindo a somente o id do usuário cadastrar task.
		var idUser = request.getAttribute("idUser");
		task.setIdUser((UUID)idUser);
		
		var currentDate = LocalDateTime.now();
		
		if (currentDate.isAfter(task.getStartAt())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		// Salvando task.
		var taskSave = this.taksRepository.save(task);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(taskSave);
	}
}
