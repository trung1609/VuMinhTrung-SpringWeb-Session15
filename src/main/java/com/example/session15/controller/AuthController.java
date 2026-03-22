package com.example.session15.controller;

import com.example.session15.dto.request.ChangeRole;
import com.example.session15.dto.request.FormLogin;
import com.example.session15.dto.request.FormRegister;
import com.example.session15.dto.response.JwtResponse;
import com.example.session15.exception.ResourceConflictException;
import com.example.session15.exception.ResourceNotFoundException;
import com.example.session15.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody FormRegister formRegister) throws ResourceConflictException {
        return new ResponseEntity<>(authService.register(formRegister), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody FormLogin formLogin) throws ResourceConflictException, ResourceNotFoundException {
        return new ResponseEntity<>(authService.login(formLogin), HttpStatus.OK);
    }


}
