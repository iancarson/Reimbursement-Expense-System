package com.andrew.ers.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.andrew.ers.dto.ExpenseDTO;
import com.andrew.ers.dto.ReimbursementAction;
import com.andrew.ers.dto.ReimbursementDTO;
import com.andrew.ers.services.ExpenseService;
import com.andrew.ers.services.ReimbursementService;
import com.andrew.ers.services.S3Service;

@RestController
@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE)
public class ReimbursementController {
	
	final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ReimbursementService rservice;
	
	@Autowired
	ExpenseService eservice;
	
	@Autowired
	S3Service s3;
	
	@GetMapping("/reimbursements")
	public ResponseEntity<?> getAllReimbursements() {
		Resources<ReimbursementDTO> list = rservice.getAllReimbursements();
		return new ResponseEntity<Resources<ReimbursementDTO>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/reimbursements/{id}")
	public ResponseEntity<?> getReimbursementById(@PathVariable long id) {
		Optional<ReimbursementDTO> r = rservice.getReimbursementById(id);
		if (r.isPresent()) {
			return new ResponseEntity<ReimbursementDTO>(r.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<ReimbursementDTO>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/users/{username}/reimbursements")
	public ResponseEntity<?> getReimbursementsForUser(@PathVariable String username) {
		Resources<ReimbursementDTO> list = rservice.getReimbursementsForUser(username);
		return new ResponseEntity<Resources<ReimbursementDTO>>(list, HttpStatus.OK);
	}
	
	@PutMapping(value="/reimbursements/{id}/status",consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> performActionOnReimbursement(@PathVariable long id, @RequestBody ReimbursementAction update) {
		System.out.println(update);
		if (update.action.equals(ReimbursementAction.approve)) {
			rservice.approveReimbursement(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else if (update.action.equals(ReimbursementAction.deny)) {
			rservice.denyReimbursement(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value="/users/{username}/reimbursements",consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createNewReimbursementForUser(@PathVariable String username, 
			@RequestBody ReimbursementDTO newReimburseRequest) {
		Resources<ReimbursementDTO> list = rservice.submitNewReimbursement(username, newReimburseRequest);
		return new ResponseEntity<Resources<ReimbursementDTO>>(list, HttpStatus.CREATED);
	}
	
	@PostMapping(value="/users/{username}/expenses/{eid}", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadReceiptForExpense(
			@PathVariable String username, @PathVariable long eid, @RequestParam MultipartFile file) {
		try {
			Optional<ExpenseDTO> eopt = eservice.addReceiptToExpenseById(eid, username, file);
			if (eopt.isPresent()) {
				HttpHeaders headers = new HttpHeaders();
				headers.setLocation(eopt.get().getReceiptURI());
				return new ResponseEntity<>(headers, HttpStatus.FOUND);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IOException | URISyntaxException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
