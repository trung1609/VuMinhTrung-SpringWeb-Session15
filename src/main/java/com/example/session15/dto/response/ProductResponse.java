package com.example.session15.dto.response;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ProductResponse {
    private String name;
    private String description;
    private BigDecimal price;
    private String size;
    private String toppings;

    public ProductResponse(String name, String description, BigDecimal price, String size, String toppings) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.size = size;
        this.toppings = toppings;
    }
}
