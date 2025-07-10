package com.lade.DTO;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

	private String id;
	@NotBlank
	@Size(min=3,max=50)
	private String username;
	
	@NotBlank
	@Size(max=70)
	@Email
	private String email;
	private Set<String>roles;
}
