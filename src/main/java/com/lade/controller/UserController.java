package com.lade.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lade.DTO.ChangePasswordDTO;
import com.lade.DTO.UserDTO;
import com.lade.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	@GetMapping("/getuserbyid/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
	    return ResponseEntity.ok(userService.getUserById(id));
	}

	@GetMapping("/getuserbyusername/{username")
	public ResponseEntity<UserDTO>getUserByUsername(@PathVariable String username)
	{
		return ResponseEntity.ok(userService.getUserByUsername(username));
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<UserDTO>> getAllUsers() {
	    return ResponseEntity.ok(userService.getAllUsers());
	}
	@PutMapping("/changepassword/{id}")
	public ResponseEntity<UserDTO> changePassword(
	        @PathVariable String id,
	        @RequestBody ChangePasswordDTO changePasswordDTO) {

	    UserDTO updatedUser = userService.changePassword(id, changePasswordDTO);

	    if (updatedUser != null) {
	        return ResponseEntity.ok(updatedUser);
	    } else {
	        return ResponseEntity.badRequest().build(); // or throw custom exception
	    }
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<UserDTO> updateUser(
	        @PathVariable String id,
	        @RequestBody UserDTO userDTO) {

	    UserDTO updatedUser = userService.updateUser(id, userDTO);
	    if (updatedUser != null) {
	        return ResponseEntity.ok(updatedUser);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUserById(@PathVariable String id) {
	        userService.deleteUserById(id);
	        return ResponseEntity.ok("User deleted successfully.");
	 }
}
