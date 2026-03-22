package com.example.session15.controller;

import com.example.session15.dto.request.PageRequestDto;
import com.example.session15.dto.request.ProductRequest;
import com.example.session15.dto.response.PageResponseDto;
import com.example.session15.dto.response.ProductResponse;
import com.example.session15.exception.ResourceNotFoundException;
import com.example.session15.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PageResponseDto<ProductResponse>> getAllProducts(@ModelAttribute PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(productService.getAllProducts(pageRequestDto));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(productService.createProduct(productRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) throws ResourceNotFoundException {
        return ResponseEntity.ok(productService.updateProduct(id, productRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
