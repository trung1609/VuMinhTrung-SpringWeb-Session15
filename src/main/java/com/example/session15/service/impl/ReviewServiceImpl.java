package com.example.session15.service.impl;

import com.example.session15.dto.request.ReviewRequest;
import com.example.session15.dto.response.ApiResponse;
import com.example.session15.dto.response.ReviewResponse;
import com.example.session15.entity.Product;
import com.example.session15.entity.Review;
import com.example.session15.entity.Users;
import com.example.session15.exception.AccessDeniedException;
import com.example.session15.exception.ResourceConflictException;
import com.example.session15.exception.ResourceNotFoundException;
import com.example.session15.repository.OrderItemRepository;
import com.example.session15.repository.ProductRepository;
import com.example.session15.repository.ReviewRepository;
import com.example.session15.repository.UserRepository;
import com.example.session15.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;


    @Override
    public ApiResponse<ReviewResponse> createReview(ReviewRequest reviewRequest, String email) throws ResourceNotFoundException, AccessDeniedException {
        Users users = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        Product product = productRepository.findById(reviewRequest.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + reviewRequest.getProductId()));

        boolean hasBought = orderItemRepository.existsByUsersAndProduct(users.getId(), product.getId());

        if (!hasBought) {
            throw new AccessDeniedException("You can only review products that you have purchased and completed orders");
        }

        if (reviewRepository.existsByUsersIdAndProductId(users.getId(), product.getId())) {
            throw new AccessDeniedException("You have already reviewed this product");
        }

        Review review = Review.builder()
                .users(users)
                .product(product)
                .rating(reviewRequest.getRating())
                .comment(reviewRequest.getComment())
                .build();
        reviewRepository.save(review);

        return ApiResponse.<ReviewResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Review created successfully")
                .data(ReviewResponse.builder()
                        .email(users.getEmail())
                        .rating(review.getRating())
                        .comment(review.getComment())
                        .createdDate(review.getCreatedDate())
                        .build())
                .build();
    }

    @Override
    public ApiResponse<List<ReviewResponse>> getReviews(Long productId) {
        return ApiResponse.<List<ReviewResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Reviews retrieved successfully")
                .data(reviewRepository.findByProductId(productId).stream().map(review -> ReviewResponse.builder()
                        .email(review.getUsers().getEmail())
                        .rating(review.getRating())
                        .comment(review.getComment())
                        .createdDate(review.getCreatedDate())
                        .build()).toList())
                .build();
    }
}
