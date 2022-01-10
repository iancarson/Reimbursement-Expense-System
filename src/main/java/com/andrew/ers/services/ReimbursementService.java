package com.andrew.ers.services;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import com.andrew.ers.controllers.ReimbursementController;
import com.andrew.ers.dto.ExpenseDTO;
import com.andrew.ers.dto.ReimbursementDTO;
import com.andrew.ers.model.AppUser;
import com.andrew.ers.model.Reimbursement;
import com.andrew.ers.repositories.ReimbursementRepo;
import com.andrew.ers.repositories.UserRepo;

@Service
public class ReimbursementService {
	
	@Autowired
	ReimbursementRepo reimbursementRepo;
	
	@Autowired
	UserRepo userRepo;
	
	public static ReimbursementDTO convert(Reimbursement r) {
		ReimbursementDTO  newr = new ReimbursementDTO();
		newr.setId(r.getId());
		newr.setExpenses(ExpenseService.convert(r.getExpenses()));
		newr.setApproved(r.isApproved());
		double tot = 0;
		for (ExpenseDTO e : newr.getExpenses()) {
			tot += e.getAmount();
		}
		newr.setTotal(tot);
		return newr;
	}
	
	public static List<ReimbursementDTO> convert(List<Reimbursement> listr) {
		List<ReimbursementDTO> listDTO = new ArrayList<>();
		for (Reimbursement re : listr) {
			listDTO.add(convert(re));
		}
		return listDTO;
	}
	
	public static Reimbursement convert(ReimbursementDTO dto) {
		Reimbursement  newr = new Reimbursement();
		newr.setId(dto.getId());
		newr.setExpenses(ExpenseService.convertDTO(dto.getExpenses()));
		newr.setApproved(dto.isApproved());
		// total not recorded in DB - can be calculated from expenses on client side
		return newr;
	}
	
	public static Reimbursement convertDTO(ReimbursementDTO r) {
		Reimbursement newr = new Reimbursement();
		newr.setId(r.getId());
		newr.setExpenses(ExpenseService.convertDTO(r.getExpenses()));
		newr.setApproved(r.isApproved());
		return newr;
	}
	
	public Resources<ReimbursementDTO> getAllReimbursements() {
		return new Resources<>(convert(reimbursementRepo.findAll()), 
				linkTo(methodOn(ReimbursementController.class).getAllReimbursements()).withSelfRel());
	}
	
	public Resources<ReimbursementDTO> getReimbursementsForUser(String username) {
		return new Resources<>(convert(userRepo.findByUsername(username).getReimbursements()), 
				linkTo(methodOn(ReimbursementController.class).getAllReimbursements()).withSelfRel());
	}
	
	public Optional<ReimbursementDTO> getReimbursementById(long id) {
		Optional<Reimbursement> ropt = reimbursementRepo.findById(id);
		if (ropt.isPresent()) {
			return Optional.of(convert(ropt.get()));
		} else {
			return Optional.ofNullable(null);
		}
	}
	
	/**
	 * Adds a new reimbursement to the user, then updates the user which
	 * persists the new reimbursement to the database 
	 * @return 
	 */
	public Resources<ReimbursementDTO> submitNewReimbursement(String username, ReimbursementDTO newR) {
		AppUser user = userRepo.findByUsername(username);
		newR.setApproved(false); // every new request is not approved by default
		Reimbursement re = convertDTO(newR);
		reimbursementRepo.save(re);
		user.addReimbursement(re);
		AppUser updatedUser = userRepo.save(user);
		List<Reimbursement> newList = updatedUser.getReimbursements();
		return new Resources<>(convert(newList), 
				linkTo(methodOn(ReimbursementController.class).getAllReimbursements()).withSelfRel());
	}
	
	/**
	 * Approves all reimbursement requests from a user
	 */
	public void approveAllReimbursements(String username) {
		AppUser user = userRepo.findByUsername(username);
		user.getReimbursements().forEach((r) -> r.setApproved(true));
		userRepo.save(user);
	}
	
	/**
	 * Approves the reimbursement request of the given id
	 * @param id
	 */
	public void approveReimbursement(long id) {
		Optional<Reimbursement> optR = reimbursementRepo.findById(id);
		if (optR.isPresent()) {
			Reimbursement r = optR.get();
			r.setApproved(true);
			reimbursementRepo.save(r);
		} else {
			throw new NoSuchElementException("Cannot find reimbursment of id " + id);
		}
	}
	
	/**
	 * Denies the reimbursement request of the given id
	 * @param id
	 */
	public void denyReimbursement(long id) {
		Optional<Reimbursement> optR = reimbursementRepo.findById(id);
		if (optR.isPresent()) {
			Reimbursement r = optR.get();
			r.setApproved(false);
			reimbursementRepo.save(r);
		} else {
			throw new NoSuchElementException("Cannot find reimbursment of id " + id);
		}
	}
}
