package com.lade.DTO;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class LoginResponseDTO {
	private String jwtToken;
	private UserDTO userDTO;
	
  
}
