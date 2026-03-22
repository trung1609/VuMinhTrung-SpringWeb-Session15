package com.example.session15.service.impl;

import com.example.session15.dto.request.ChangeRole;
import com.example.session15.dto.request.FormLogin;
import com.example.session15.dto.request.FormRegister;
import com.example.session15.dto.response.JwtResponse;
import com.example.session15.entity.Role;
import com.example.session15.entity.Users;
import com.example.session15.exception.ResourceConflictException;
import com.example.session15.exception.ResourceNotFoundException;
import com.example.session15.repository.UserRepository;
import com.example.session15.security.jwt.JwtProvider;
import com.example.session15.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt_expire}")
    private long expire;

    @Override
    public String register(FormRegister formRegister) throws ResourceConflictException {
        if (userRepository.existsByEmail(formRegister.getEmail())) {
            throw new ResourceConflictException("Email is already in use");
        }

        if (userRepository.existsByPhone(formRegister.getPhone())) {
            throw new ResourceConflictException("Phone number is already in use");
        }
        Users users = new Users();
        if (formRegister.getRole() == null){
            users.setRole(Role.ROLE_USER);
        }else {
            users.setRole(Role.valueOf(formRegister.getRole()));
        }

        users.setEmail(formRegister.getEmail());
        users.setPassword(passwordEncoder.encode(formRegister.getPassword()));
        users.setPhone(formRegister.getPhone());
        userRepository.save(users);
        return "User registered successfully";
    }

    @Override
    public JwtResponse login(FormLogin formLogin) throws ResourceConflictException, ResourceNotFoundException {
        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            formLogin.getEmail(),
                            formLogin.getPassword()
                    )
            );
        }catch (AuthenticationException e) {
            throw new ResourceConflictException("Invalid username or password");
        }

        String accessToken = jwtProvider.generateAccessToken(formLogin.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(formLogin.getEmail());

        return JwtResponse.builder()
                .username(formLogin.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expire(new Date(new Date().getTime() + expire))
                .users(userRepository.findByEmail(formLogin.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + formLogin.getEmail())))
                .build();
    }
}
