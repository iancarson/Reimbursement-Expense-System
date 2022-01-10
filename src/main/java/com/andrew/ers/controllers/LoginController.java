package com.andrew.ers.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.andrew.ers.dto.LoginCredentials;
import com.andrew.ers.dto.UserDTO;
import com.andrew.ers.model.AppUser;
import com.andrew.ers.services.AuthService;
import com.andrew.ers.services.UserService;

@RestController
@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
public class LoginController {
	
	@Autowired
	AuthService authService;
	
	@Autowired
	UserService userService;
	
	@Value("${spring.profiles.active}")
	String env;
	
	@RequestMapping(produces=MediaType.TEXT_PLAIN_VALUE, consumes=MediaType.ALL_VALUE, 
			method=RequestMethod.GET, path="/env")
	public String getEnvironment() {
		return env;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO userDTO) {
		AppUser userEntity = new AppUser();
		if (userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
			// TODO: encrypt here?
			userEntity = UserService.convert(userDTO);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		userEntity.setEmail(userDTO.getEmail());
		userEntity.setAddress(userDTO.getAddress());
		AppUser returningUser = userService.registerNewUser(userEntity);
		return new ResponseEntity<>(returningUser, HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginCredentials userCreds) {
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
