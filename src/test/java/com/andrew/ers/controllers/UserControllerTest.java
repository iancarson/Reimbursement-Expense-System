package com.andrew.ers.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import com.andrew.ers.dto.UserDTO;

public class UserControllerTest extends BaseControllerTest {
	
	UserDTO testUser;
	
	@Before
	public void setup() {
		testUser = new UserDTO();
		testUser.setFirstName("Andrew");
		testUser.setLastName("Crenwelge");
		testUser.setPassword("password");
		testUser.setConfirmPassword("password");
		testUser.setAddress("123abc");
		testUser.setEmail("abc@gmail.com");
	}
	
	@Test
	public void testGetUsers() throws Exception {
		mvc.perform(get("/users"))
		  .andExpect(status().isOk())
		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void testCreateUsers() throws Exception {
		String json = mapper.writeValueAsString(testUser);
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
		   .andExpect(status().isCreated());
	}
	
	@Test
	public void testBadPassword() throws Exception {
		testUser.setConfirmPassword("badpassword");
		String json = mapper.writeValueAsString(testUser);
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
		   .andExpect(status().isBadRequest());
	}
	
}
