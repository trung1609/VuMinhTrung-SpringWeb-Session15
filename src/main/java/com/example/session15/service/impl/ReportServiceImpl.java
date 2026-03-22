package com.example.session15.service.impl;

import com.example.session15.dto.response.RevenueResponse;
import com.example.session15.repository.OrderRepository;
import com.example.session15.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final OrderRepository orderRepository;

    @Override
    public List<RevenueResponse> getRevenue(String type) {
        return switch (type.toLowerCase()) {
            case "day" -> orderRepository.getRevenueByDay();
            case "month" -> orderRepository.getRevenueByMonth();
            case "year" -> orderRepository.getRevenueByYear();
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }
}
