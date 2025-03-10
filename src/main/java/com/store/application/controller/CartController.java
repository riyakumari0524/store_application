package com.store.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.store.application.dto.*;
import com.store.application.entity.User;
import com.store.application.service.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserSessionService userSessionService;

    public CartController(CartService cartService, UserSessionService userSessionService) {
        this.cartService = cartService;
        this.userSessionService = userSessionService;
    }

    @Operation(summary = "Add Item to Cart", description = "Allows a user to add a product to the cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid productId or quantity"),
            @ApiResponse(responseCode = "401", description = "User not logged in"),
            @ApiResponse(responseCode = "404", description = "User or product not found")
    })
    @PostMapping("/items")
    public ResponseEntity<?> addItemToCart(@Valid @RequestBody CartItemRequest request, HttpSession session) {
        User user = userSessionService.getUserFromSession(session);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in. Please log in first.");
        }

        try {
            UserCartResponse result = cartService.addItemToCart(user, request.getProductId(), request.getQuantity());
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Modify Item in Cart", description = "Modify the quantity of an existing product in the cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated"),
            @ApiResponse(responseCode = "400", description = "Invalid productId or quantity"),
            @ApiResponse(responseCode = "401", description = "User not logged in"),
            @ApiResponse(responseCode = "404", description = "User, cart, or product not found")
    })
    @PutMapping("/items/{productId}")
    public ResponseEntity<?> modifyItemInCart(@PathVariable Long productId, @Valid @RequestBody CartItemRequest request, HttpSession session) {
        User user = userSessionService.getUserFromSession(session);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        try {
            UserCartResponse result = cartService.modifyItemInCart(user, productId, request.getQuantity());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Remove Item from Cart", description = "Removes a product from the cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item removed"),
            @ApiResponse(responseCode = "400", description = "Invalid productId"),
            @ApiResponse(responseCode = "401", description = "User not logged in"),
            @ApiResponse(responseCode = "404", description = "User or product not found")
    })
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable Long itemId, HttpSession session) {
        User user = userSessionService.getUserFromSession(session);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        try {
            UserCartResponse result = cartService.removeItemFromCart(user, itemId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "View Cart Details", description = "Fetches the current cart details for the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart details fetched"),
            @ApiResponse(responseCode = "401", description = "User not logged in"),
            @ApiResponse(responseCode = "404", description = "User or cart not found")
    })
    @GetMapping
    public ResponseEntity<?> viewCartDetails(HttpSession session) {
        User user = userSessionService.getUserFromSession(session);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        try {
            CartResponse cart = cartService.viewCartDetails(user);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
