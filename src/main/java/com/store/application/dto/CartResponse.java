package com.store.application.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private List<CartItemDetails> cartItems;
    private BigDecimal total;
}
