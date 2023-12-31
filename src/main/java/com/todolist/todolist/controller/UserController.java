package com.todolist.todolist.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.todolist.model.User;
import com.todolist.todolist.repository.UserRepository;



@RestController
@RequestMapping("/users")
public class UserController {

	
	@Autowired
	private UserRepository userRepository;
	

	@PostMapping("/")
    public ResponseEntity<User> create(@RequestBody User user) {
        // Verifica se o nome de usuário já existe
        var userModel = userRepository.findByUsername(user.getUsername());

        if (userModel != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Hash da senha usando BCrypt
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hashedPassword);

        // Salva o usuário no banco de dados
        var userCreated = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
	
}
