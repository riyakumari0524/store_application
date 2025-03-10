package com.store.application.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDetails {
    private int ordinal;
    private String title;
    private BigDecimal price;
    private int quantity;
    private BigDecimal subtotal;
}
