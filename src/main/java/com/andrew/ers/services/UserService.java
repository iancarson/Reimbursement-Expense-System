package com.andrew.ers.services;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.andrew.ers.controllers.UserController;
import com.andrew.ers.dto.UserDTO;
import com.andrew.ers.exceptions.UserAlreadyExistsException;
import com.andrew.ers.model.AppUser;
import com.andrew.ers.model.AppUserPrincipal;
import com.andrew.ers.repositories.UserRepo;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	UserRepo userRepo;
	
	public static UserDTO convert(AppUser user) {
		UserDTO udto = new UserDTO();
		udto.setId(user.getId());
		udto.setUsername(user.getUsername());
		udto.setFirstName(user.getFirstName());
		udto.setLastName(user.getLastName());
		udto.setConfirmPassword(null);
		udto.setPassword(user.getPassword());
		udto.setEmail(user.getEmail());
		udto.setAddress(user.getAddress());
		udto.setReimbursements(user.getReimbursements());
		return udto;
	}
	
	public static List<UserDTO> convert(List<AppUser> users) {
		List<UserDTO> dtoList = new ArrayList<>();
		for (AppUser au : users) {
			dtoList.add(convert(au));
		}
		return dtoList;
	}
	
	public static AppUser convert(UserDTO userDTO) {
		AppUser userEntity = new AppUser();
		userEntity.setId(userDTO.getId());
		userEntity.setFirstName(userDTO.getFirstName());
		userEntity.setLastName(userDTO.getLastName());
		userEntity.setUsername(userDTO.getUsername());
		userEntity.setPassword(userDTO.getPassword());
		userEntity.setEmail(userDTO.getEmail());
		userEntity.setAddress(userDTO.getAddress());
		userEntity.setReimbursements(userDTO.getReimbursements());
		return userEntity;
	}
	
	public Resources<UserDTO> getAllUserResources() {
		return new Resources<>(convert(userRepo.findAll()),
				linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
	}
	
	public Resource<UserDTO> getUserResourceById(long id) {
		return new Resource<>(convert(userRepo.getOne(id)),
				linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
	}
	
	public AppUser registerNewUser(AppUser user) {
		Example<AppUser> e = Example.of(user);
		if (userRepo.exists(e)) {
			throw new UserAlreadyExistsException();
		} else {
			return userRepo.save(user);
		}
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new AppUserPrincipal(user);
	}
	
	public void deleteUser(long id) {
		userRepo.deleteById(id);
	}
	
	public void saveUser(UserDTO userToSave) {
		userRepo.save(convert(userToSave));
	}
}
