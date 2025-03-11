package com.store.application.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.store.application.dto.*;
import com.store.application.entity.User;
import com.store.application.service.*;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private SessionManagementService userSessionService;

    @InjectMocks
    private CartController cartController;

    @Mock
    private HttpSession session;

    private User user;
    private CartItemRequest cartItemRequest;
    private UserCartResponse userCartResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("testuser@example.com");

        cartItemRequest = new CartItemRequest();
        cartItemRequest.setProductId(101L);
        cartItemRequest.setQuantity(2);

        userCartResponse = new UserCartResponse();
    }

    @Test
    void testAddItemToCart_Success() {
        when(userSessionService.getUserFromSession(session)).thenReturn(user);
        when(cartService.addItemToCart(user, cartItemRequest.getProductId(), cartItemRequest.getQuantity()))
                .thenReturn(userCartResponse);

        ResponseEntity<?> response = cartController.addItemToCart(cartItemRequest, session);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testModifyItemInCart_Success() {
        Long productId = 101L;
        when(userSessionService.getUserFromSession(session)).thenReturn(user);
        when(cartService.modifyItemInCart(user, productId, cartItemRequest.getQuantity()))
                .thenReturn(userCartResponse);

        ResponseEntity<?> response = cartController.modifyItemInCart(productId, cartItemRequest, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testRemoveItemFromCart_Success() {
        Long itemId = 101L;
        when(userSessionService.getUserFromSession(session)).thenReturn(user);
        when(cartService.removeItemFromCart(user, itemId)).thenReturn(userCartResponse);

        ResponseEntity<?> response = cartController.removeItemFromCart(itemId, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testViewCartDetails_Success() {
        when(userSessionService.getUserFromSession(session)).thenReturn(user);
        CartItemDetails cartItem1 = new CartItemDetails(1, "Product 1", new BigDecimal("25.00"), 2, new BigDecimal("50.00"));
        CartItemDetails cartItem2 = new CartItemDetails(2, "Product 2", new BigDecimal("50.00"), 1, new BigDecimal("50.00"));
        List<CartItemDetails> cartItems = Arrays.asList(cartItem1, cartItem2);
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartItems(cartItems);
        cartResponse.setTotal(new BigDecimal("100.00"));

        when(cartService.viewCartDetails(user)).thenReturn(cartResponse);

        ResponseEntity<?> response = cartController.viewCartDetails(session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(cartResponse, response.getBody());
    }
}
