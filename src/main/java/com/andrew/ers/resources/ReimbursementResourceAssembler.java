package com.andrew.ers.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;

import com.andrew.ers.controllers.UserController;
import com.andrew.ers.model.Reimbursement;

public class ReimbursementResourceAssembler implements ResourceAssembler<Reimbursement, Resource<Reimbursement>> {

	@Override
	public Resource<Reimbursement> toResource(Reimbursement reimbursement) {
		return new Resource<>(reimbursement,
				linkTo(methodOn(UserController.class).getUser(reimbursement.getId())).withSelfRel(),
				linkTo(methodOn(UserController.class).getAllUsers()).withRel("Reimbursements"));
	}

}
