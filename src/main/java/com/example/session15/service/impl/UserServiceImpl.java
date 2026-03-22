package com.example.session15.service.impl;

import com.example.session15.dto.request.ChangeRole;
import com.example.session15.dto.response.UserResponse;
import com.example.session15.entity.Role;
import com.example.session15.entity.Users;
import com.example.session15.exception.ResourceNotFoundException;
import com.example.session15.repository.UserRepository;
import com.example.session15.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public String changeRole(Long id,  ChangeRole changeRole) throws ResourceNotFoundException {
        Users users = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + id));
        users.setRole(Role.valueOf(changeRole.getRole()));
        userRepository.save(users);
        return "Role changed successfully";
    }

    @Override
    public UserResponse getMyInfo(String email) throws ResourceNotFoundException {
        Users users = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return UserResponse.builder()
                .email(users.getEmail())
                .phone(users.getPhone())
                .role(users.getRole().name())
                .build();
    }



}
