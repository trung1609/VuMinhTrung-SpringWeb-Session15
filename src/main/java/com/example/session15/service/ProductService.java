package com.example.session15.service;

import com.example.session15.dto.request.PageRequestDto;
import com.example.session15.dto.request.ProductRequest;
import com.example.session15.dto.response.ApiResponse;
import com.example.session15.dto.response.PageResponseDto;
import com.example.session15.dto.response.ProductResponse;
import com.example.session15.exception.ResourceNotFoundException;

public interface ProductService {
    ApiResponse<ProductResponse> createProduct(ProductRequest request);
    ApiResponse<ProductResponse> updateProduct(Long id, ProductRequest request) throws ResourceNotFoundException;
    String deleteProduct(Long id) throws ResourceNotFoundException;
    PageResponseDto<ProductResponse> getAllProducts(PageRequestDto pageRequestDto);
}
