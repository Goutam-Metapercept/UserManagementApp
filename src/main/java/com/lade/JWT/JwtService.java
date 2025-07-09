package com.lade.JWT;

import org.springframework.stereotype.Service;
import com.lade.UserManagementApp;
import com.lade.entity.User;

@Service
public class JwtService {

    private final UserManagementApp userManagementApp;
	
	private String secretKey;
	private Long jwtexpiration;


    JwtService(UserManagementApp userManagementApp) {
        this.userManagementApp = userManagementApp;
    }
	
	
    public String extractUserId(String jwtToken) {
        return extractClaim(jwtToken, claims -> claims.get("userId", String.class));
    }


	


	private String extractClaim(String jwtToken, Object object) {
		// TODO Auto-generated method stub
		return null;
	}


	

	public boolean isTokenValid(String jwtToken, User userDetails) {
		// TODO Auto-generated method stub
		return false;
	}

	public String generateToken(User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
