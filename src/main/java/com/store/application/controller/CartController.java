package com.store.application.controller;

import com.store.application.exception.UnauthorizedAccessException;
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
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final SessionManagementService userSessionService;

    private User getUserFromSession(HttpSession session) {
        User user = userSessionService.getUserFromSession(session);
        if (user == null) {
            throw new UnauthorizedAccessException(Constants.UNAUTHORIZED_ACCESS);
        }
        return user;
    }

    @Operation(summary = "Add Item to Cart", description = "Allows a user to add a product to the cart.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid productId or quantity"),
            @ApiResponse(responseCode = "401", description = "User not logged in"),
            @ApiResponse(responseCode = "404", description = "User or product not found")
    })
    @PostMapping("/item")
    public ResponseEntity<UserCartResponse> addItemToCart(@Valid @RequestBody CartItemRequest request, HttpSession session) {
        User user = getUserFromSession(session);
        UserCartResponse result = cartService.addItemToCart(user, request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Modify Item in Cart", description = "Modify the quantity of an existing product in the cart.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item updated"),
            @ApiResponse(responseCode = "400", description = "Invalid productId or quantity"),
            @ApiResponse(responseCode = "401", description = "User not logged in"),
            @ApiResponse(responseCode = "404", description = "User, cart, or product not found")
    })
    @PutMapping("/item/{productId}")
    public ResponseEntity<UserCartResponse> modifyItemInCart(@PathVariable Long productId, @Valid @RequestBody CartItemRequest request, HttpSession session) {
        User user = getUserFromSession(session);
        UserCartResponse result = cartService.modifyItemInCart(user, productId, request.getQuantity());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Remove Item from Cart", description = "Removes a product from the cart.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item removed"),
            @ApiResponse(responseCode = "400", description = "Invalid productId"),
            @ApiResponse(responseCode = "401", description = "User not logged in"),
            @ApiResponse(responseCode = "404", description = "User or product not found")
    })
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<UserCartResponse> removeItemFromCart(@PathVariable Long itemId, HttpSession session) {
        User user = getUserFromSession(session);
        UserCartResponse result = cartService.removeItemFromCart(user, itemId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "View Cart Details", description = "Fetches the current cart details for the user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart details fetched"),
            @ApiResponse(responseCode = "401", description = "User not logged in"),
            @ApiResponse(responseCode = "404", description = "User or cart not found")
    })
    @GetMapping
    public ResponseEntity<CartResponse> viewCartDetails(HttpSession session) {
        User user = getUserFromSession(session);
        CartResponse cart = cartService.viewCartDetails(user);
        return ResponseEntity.ok(cart);
    }
}
