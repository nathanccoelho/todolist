package com.todolist.todolist.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todolist.todolist.model.User;

public interface UserRepository extends JpaRepository<User, UUID>{
	
	User findByUsername (String username);

}
