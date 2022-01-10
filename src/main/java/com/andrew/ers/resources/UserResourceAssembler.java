package com.andrew.ers.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;

import com.andrew.ers.controllers.UserController;
import com.andrew.ers.model.AppUser;

public class UserResourceAssembler implements ResourceAssembler<AppUser, Resource<AppUser>> {

	@Override
	public Resource<AppUser> toResource(AppUser user) {
		return new Resource<>(user,
				linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel(),
				linkTo(methodOn(UserController.class).getAllUsers()).withRel("Users"));
	}

}
