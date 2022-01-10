package com.andrew.ers.controllers;

import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(Enclosed.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class BaseControllerTest {
	
	@Autowired
	MockMvc mvc;
	
	@Autowired
	ObjectMapper mapper;
	
	protected static final String testUsername = "acrenwelge";

}
