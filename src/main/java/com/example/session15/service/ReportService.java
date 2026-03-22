package com.example.session15.service;

import com.example.session15.dto.response.RevenueResponse;

import java.util.List;

public interface ReportService {
    List<RevenueResponse> getRevenue(String type);
}
