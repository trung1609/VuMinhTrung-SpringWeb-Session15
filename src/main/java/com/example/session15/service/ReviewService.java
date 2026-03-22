package com.example.session15.service;

import com.example.session15.dto.request.ReviewRequest;
import com.example.session15.dto.response.ApiResponse;
import com.example.session15.dto.response.ReviewResponse;
import com.example.session15.exception.AccessDeniedException;
import com.example.session15.exception.ResourceConflictException;
import com.example.session15.exception.ResourceNotFoundException;

import javax.swing.event.ListDataEvent;
import java.util.List;

public interface ReviewService {
    ApiResponse<ReviewResponse> createReview(ReviewRequest reviewRequest, String email) throws ResourceNotFoundException, ResourceConflictException, AccessDeniedException;
    ApiResponse<List<ReviewResponse>> getReviews(Long productId);
}
