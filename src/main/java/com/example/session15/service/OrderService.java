package com.example.session15.service;

import com.example.session15.dto.request.OrderRequest;
import com.example.session15.dto.request.PageRequestDto;
import com.example.session15.dto.response.OrderResponse;
import com.example.session15.dto.response.PageResponseDto;
import com.example.session15.entity.OrderStatus;
import com.example.session15.exception.ResourceNotFoundException;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest, String email) throws ResourceNotFoundException;
    PageResponseDto<OrderResponse> getAllMyOrders(String email, PageRequestDto pageRequestDto) throws ResourceNotFoundException;
    PageResponseDto<OrderResponse> getAllOrders(PageRequestDto pageRequestDto);
    String updateOrderStatus(Long id, OrderStatus status) throws ResourceNotFoundException;
}
