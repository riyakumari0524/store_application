package com.store.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.application.dto.*;
import com.store.application.entity.*;
import com.store.application.service.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final UserSessionService userSessionService;

    public OrderController(OrderService orderService, UserSessionService userSessionService) {
        this.orderService = orderService;
        this.userSessionService = userSessionService;
    }

    @Operation(summary = "Checkout", description = "This endpoint processes the checkout for a logged-in user and creates an order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully placed"),
            @ApiResponse(responseCode = "401", description = "User not logged in"),
            @ApiResponse(responseCode = "400", description = "Business-related error during checkout")
    })
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> checkout(HttpSession session) {
        User user = userSessionService.getUserFromSession(session);
        OrderResponse result = orderService.checkout(user);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}

