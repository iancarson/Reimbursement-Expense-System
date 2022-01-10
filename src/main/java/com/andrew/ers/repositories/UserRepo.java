package com.andrew.ers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andrew.ers.model.AppUser;

public interface UserRepo extends JpaRepository<AppUser, Long> {
	
	AppUser findByUsername(String username);
}
