package com.store.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.store.application.dto.*;
import com.store.application.entity.*;
import com.store.application.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final SessionManagementService userSessionService;

    @Operation(summary = "Checkout", description = "Processes checkout for a logged-in user and creates an order.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order successfully placed"),
            @ApiResponse(responseCode = "401", description = "User not logged in"),
            @ApiResponse(responseCode = "400", description = "Business-related error during checkout")
    })
    @PostMapping
    public ResponseEntity<?> checkout(HttpSession session) {
        User user = userSessionService.getUserFromSession(session);

        if (user == null) {
            log.warn("Checkout attempt failed: User not logged in.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in. Please log in first.");
        }

        try {
            log.info("User [{}] initiating checkout.", user.getId());
            OrderResponse result = orderService.checkout(user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Checkout failed for user [{}]: {}", user.getId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}


