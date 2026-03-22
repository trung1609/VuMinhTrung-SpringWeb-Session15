package com.example.session15.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueResponse {
    private String time;
    private BigDecimal total;
}
