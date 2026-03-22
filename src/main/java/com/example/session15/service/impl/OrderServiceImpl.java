package com.example.session15.service.impl;

import com.example.session15.dto.request.OrderRequest;
import com.example.session15.dto.request.PageRequestDto;
import com.example.session15.dto.response.OrderResponse;
import com.example.session15.dto.response.PageResponseDto;
import com.example.session15.dto.response.ProductResponse;
import com.example.session15.entity.*;
import com.example.session15.exception.ResourceNotFoundException;
import com.example.session15.repository.OrderRepository;
import com.example.session15.repository.ProductRepository;
import com.example.session15.repository.UserRepository;
import com.example.session15.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, String email) throws ResourceNotFoundException {
        Users users = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + email)
        );

        Order order = new Order();
        order.setUser(users);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedDate(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();


        Map<Long, OrderItem> map = new HashMap<>();
        for (OrderRequest.Item i : orderRequest.getItems()) {
            Product product = productRepository.findById(i.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + i.getProductId()));

            if (map.containsKey(product.getId())) {
                OrderItem existingItem = map.get(product.getId());
                existingItem.setQuantity(existingItem.getQuantity() + i.getQuantity());
            } else {
                OrderItem item = OrderItem.builder()
                        .product(product)
                        .quantity(i.getQuantity())
                        .priceBuy(product.getPrice())
                        .order(order)
                        .build();
                map.put(product.getId(), item);
            }
        }
        List<OrderItem> orderItems = new ArrayList<>(map.values());
        orderItems.forEach(i -> i.setOrder(order));
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            BigDecimal price = item.getPriceBuy();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());

            total = total.add(price.multiply(quantity));
        }
        order.setOrderItems(orderItems);
        order.setTotalMoney(total);
        orderRepository.save(order);

        return OrderResponse.builder()
                .totalMoney(order.getTotalMoney())
                .status(order.getStatus().name())
                .createdDate(order.getCreatedDate())
                .build();
    }

    @Override
    public PageResponseDto<OrderResponse> getAllMyOrders(String email, PageRequestDto pageRequestDto) throws ResourceNotFoundException {
        Sort sort = null;
        if (pageRequestDto.getPage() == null){
            pageRequestDto.setPage(0);
        }else {
            pageRequestDto.setPage(pageRequestDto.getPage());
        }

        if (pageRequestDto.getSize() == null){
            pageRequestDto.setSize(5);
        }else {
            pageRequestDto.setSize(pageRequestDto.getSize());
        }

        if (pageRequestDto.getSort() == null){
            sort = Sort.by("id");
        }else {
            sort = Sort.by(pageRequestDto.getSort());
        }

        if (pageRequestDto.getDirection() == null){
            sort = sort.ascending();
        }else {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageRequestDto.getPage(), pageRequestDto.getSize(), sort);

        Users users = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        Page<OrderResponse> orderResponsePage = orderRepository.findByUserId(users.getId(), pageable)
                .map(order -> OrderResponse.builder()
                        .totalMoney(order.getTotalMoney())
                        .status(order.getStatus().name())
                        .createdDate(order.getCreatedDate())
                        .build());
        return PageResponseDto.<OrderResponse>builder()
                .data(orderResponsePage.getContent())
                .page(orderResponsePage.getNumber())
                .size(orderResponsePage.getSize())
                .totalElements(orderResponsePage.getTotalElements())
                .totalPages(orderResponsePage.getTotalPages())
                .build();
    }

    @Override
    public PageResponseDto<OrderResponse> getAllOrders(PageRequestDto pageRequestDto) {
        Sort sort = null;
        if (pageRequestDto.getPage() == null){
            pageRequestDto.setPage(0);
        }else {
            pageRequestDto.setPage(pageRequestDto.getPage());
        }

        if (pageRequestDto.getSize() == null){
            pageRequestDto.setSize(5);
        }else {
            pageRequestDto.setSize(pageRequestDto.getSize());
        }

        if (pageRequestDto.getSort() == null){
            sort = Sort.by("id");
        }else {
            sort = Sort.by(pageRequestDto.getSort());
        }

        if (pageRequestDto.getDirection() == null){
            sort = sort.ascending();
        }else {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageRequestDto.getPage(), pageRequestDto.getSize(), sort);

        Page<OrderResponse> orderResponses = orderRepository.findAll(pageable)
                .map(order -> OrderResponse.builder()
                        .totalMoney(order.getTotalMoney())
                        .status(order.getStatus().name())
                        .createdDate(order.getCreatedDate())
                        .build());

        return PageResponseDto.<OrderResponse>builder()
                .data(orderResponses.getContent())
                .page(orderResponses.getNumber())
                .size(orderResponses.getSize())
                .totalElements(orderResponses.getTotalElements())
                .totalPages(orderResponses.getTotalPages())
                .build();
    }

    @Override
    public String updateOrderStatus(Long id, OrderStatus status) throws ResourceNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        order.setStatus(status);
        orderRepository.save(order);
        return "Order status updated successfully";
    }
}
