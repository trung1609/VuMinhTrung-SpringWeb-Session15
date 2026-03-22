package com.example.session15.service;

import com.example.session15.dto.request.ChangeRole;
import com.example.session15.dto.request.FormLogin;
import com.example.session15.dto.request.FormRegister;
import com.example.session15.dto.response.JwtResponse;
import com.example.session15.entity.Role;
import com.example.session15.exception.ResourceConflictException;
import com.example.session15.exception.ResourceNotFoundException;

public interface AuthService {
    String register(FormRegister formRegister) throws ResourceConflictException;
    JwtResponse login(FormLogin formLogin) throws ResourceConflictException, ResourceNotFoundException;

}
