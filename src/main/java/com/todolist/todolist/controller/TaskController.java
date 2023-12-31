package com.todolist.todolist.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.todolist.model.Task;
import com.todolist.todolist.repository.TaskRepository;
import com.todolist.todolist.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

	
	@Autowired
	private TaskRepository taksRepository;
	
	@GetMapping("/")
	public List<Task> list(HttpServletRequest request) {
		var idUser = request.getAttribute("idUser");
		var tasks = this.taksRepository.findByIdUser((UUID)idUser);
		
		return  tasks;
	}
	
	
	@PostMapping("/")
	public ResponseEntity<Task> create(@RequestBody Task task, HttpServletRequest request){
		// Restringindo a somente o id do usuário cadastrar task.
		var idUser = request.getAttribute("idUser");
		task.setIdUser((UUID)idUser);
		
		var currentDate = LocalDateTime.now();
		
		if (currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getEndAt()) || task.getStartAt().isAfter(task.getEndAt())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		// Salvando task.
		var taskSave = this.taksRepository.save(task);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(taskSave);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Task> update(@RequestBody Task task, HttpServletRequest request, @PathVariable UUID id){
		
		
		var taskExists = this.taksRepository.findById(id).orElse(null);
		
		
		if(taskExists == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		var idUser = request.getAttribute("idUser");
		
		if(!taskExists.getIdUser().equals(idUser)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		
		Utils.copyNullProperties(task, taskExists);

		var taskSave = this.taksRepository.save(taskExists);
		return ResponseEntity.status(HttpStatus.CREATED).body(taskSave);
	}
	
	
	
	
	
}


