package com.andrew.ers;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.andrew.ers.model.AppUser;
import com.andrew.ers.repositories.UserRepo;

@RunWith(Enclosed.class)
@SpringBootTest
public class UserTests {
	
	@Autowired
	UserRepo userRepo;
	
	@Test
	public void insertUser() {
		AppUser newUser = new AppUser();
		newUser.setFirstName("Andrew");
		newUser.setLastName("Crenwelge");
		newUser.setAddress("123 Main St");
		newUser.setEmail("andrew@me.com");
		newUser.setId(1L);
		newUser.setUsername("andrew");
		newUser.setPassword("password");
		userRepo.save(newUser);
		AppUser compareMe = userRepo.findById(1L).get();
		assertEquals(newUser, compareMe);
	}
}
