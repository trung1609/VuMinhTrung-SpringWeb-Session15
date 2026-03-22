package com.example.session15.controller;

import com.example.session15.dto.request.OrderRequest;
import com.example.session15.dto.request.PageRequestDto;
import com.example.session15.entity.OrderStatus;
import com.example.session15.exception.ResourceNotFoundException;
import com.example.session15.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, Authentication authentication) throws ResourceNotFoundException {
        String email = authentication.getName();
        return ResponseEntity.ok(orderService.createOrder(orderRequest, email));
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> getMyOrders(Authentication authentication, @ModelAttribute PageRequestDto pageRequestDto) throws ResourceNotFoundException {
        String email = authentication.getName();
        return ResponseEntity.ok(orderService.getAllMyOrders(email, pageRequestDto));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<?> getAllOrders(@ModelAttribute PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(orderService.getAllOrders(pageRequestDto));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestParam OrderStatus status) throws ResourceNotFoundException {
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok("Updated order status successfully");
    }
}
