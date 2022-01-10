package com.andrew.ers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andrew.ers.dto.UserDTO;
import com.andrew.ers.services.UserService;

@RestController
@RequestMapping(value="/users", produces="application/json")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping
	public ResponseEntity<?> getAllUsers() {
		Resources<UserDTO> users = userService.getAllUserResources();
		return new ResponseEntity<Resources<UserDTO>>(users, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable long id) {
		return new ResponseEntity<>(userService.getUserResourceById(id), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> createNewUser(@RequestBody UserDTO newUser) {
		if (! newUser.getPassword().equals(newUser.getConfirmPassword())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		userService.registerNewUser(UserService.convert(newUser));
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody UserDTO updatedUser) {
		userService.saveUser(updatedUser);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deactivateUser(@PathVariable long id) {
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
