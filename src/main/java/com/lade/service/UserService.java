package com.lade.service;

import com.lade.DTO.ChangePasswordDTO;
import com.lade.DTO.UserDTO;
import com.lade.entity.User;
import com.lade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Convert User → UserDTO
    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return toDTO(user);
    }


    public UserDTO getUserByUsername(String username) {
        User user= userRepository.findByUsername(username)
        		.orElseThrow(()->new RuntimeException("User not found"));
        return toDTO(user);
    }
    public List<UserDTO> getAllUsers() {
        List<User>listOfUsers= userRepository.findAll();
             return  listOfUsers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        
    }
    public UserDTO changePassword(String id, ChangePasswordDTO changePasswordDTO) {
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        return toDTO(userRepository.save(user));
    }


    public UserDTO updateUser(String id, UserDTO userDTO) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            
            return toDTO(userRepository.save(user));
        }
        return null;
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }
}
