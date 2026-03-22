package com.example.session15.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private String name;


    private String description;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private String size;

    private String toppings;
}
