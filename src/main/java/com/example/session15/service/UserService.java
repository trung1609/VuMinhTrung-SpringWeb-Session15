package com.example.session15.service;

import com.example.session15.dto.request.ChangeRole;
import com.example.session15.dto.response.UserResponse;
import com.example.session15.exception.ResourceNotFoundException;

public interface UserService {
    String changeRole(Long id, ChangeRole changeRole) throws ResourceNotFoundException;
    UserResponse getMyInfo(String email) throws ResourceNotFoundException;
}
