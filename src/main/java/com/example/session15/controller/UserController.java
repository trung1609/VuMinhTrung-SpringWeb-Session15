package com.example.session15.controller;

import com.example.session15.dto.request.ChangeRole;
import com.example.session15.exception.ResourceConflictException;
import com.example.session15.exception.ResourceNotFoundException;
import com.example.session15.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}/role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> changeRole(@PathVariable Long id, @Valid @RequestBody ChangeRole changeRole) throws ResourceConflictException, ResourceNotFoundException {
        return new ResponseEntity<>(userService.changeRole(id,changeRole), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(Authentication authentication) throws ResourceNotFoundException {
        String email = authentication.getName();
        return new ResponseEntity<>(userService.getMyInfo(email), HttpStatus.OK);
    }
}
