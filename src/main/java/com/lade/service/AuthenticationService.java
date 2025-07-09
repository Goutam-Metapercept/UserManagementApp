package com.lade.service;

import com.lade.DTO.*;
import com.lade.JWT.JwtService;
import com.lade.entity.User;
import com.lade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerRequest.Headers;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtService jwtService;

    public UserDTO registerNormalUser(RegisterRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("User is already registered");
        }

       Set<String>set=new HashSet<String>();
       set.add("ROLE_USER");
       
       User user=new User();
       user.setUsername(request.getUsername());
       user.setEmail(request.getEmail());
       user.setPasssword(passwordEncoder.encode(request.getPassword()));
       user.setRoles(set);
      
       User savedUser=userRepository.save(user);
       return toDTO(savedUser);
        
    }
    public UserDTO registerAdminUser(RegisterRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("User is already registered");
        }

       Set<String>set=new HashSet<String>();
       set.add("ROLE_ADMIN");
       set.add("ROLE_USER");
       
       
       User user=new User();
       user.setUsername(request.getUsername());
       user.setEmail(request.getEmail());
       user.setPasssword(passwordEncoder.encode(request.getPassword()));
       user.setRoles(set);
      
       User savedUser=userRepository.save(user);
       return toDTO(savedUser);
        
    }
    
    public LoginResponseDTO login(LoginRequestDTO request) {
        // Authenticate user using Spring Security
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        // Fetch user from DB
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT token
        String jwtToken = jwtService.generateToken(user);

        // Return response DTO with token and user details
        return LoginResponseDTO.builder()
                .jwtToken(jwtToken)
                .userDTO(toDTO(user))
                .build();
    }

    public ResponseEntity<String> logout()
    {
        // Create an expired cookie to clear JWT
        ResponseCookie cookie = ResponseCookie.from("JWT", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // expires immediately
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body("Logged out successfully");
    }
  
    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }

   
}
