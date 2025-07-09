package com.lade.DTO;

import lombok.Data;

@Data
public class ChangePasswordDTO {
	
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
