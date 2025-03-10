package com.store.application.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.store.application.dto.OrderResponse;
import com.store.application.entity.User;
import com.store.application.service.OrderService;
import com.store.application.service.UserSessionService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private UserSessionService userSessionService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private OrderController orderController;

    private User user;
    private OrderResponse orderResponse;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        orderResponse = new OrderResponse();
        orderResponse.setOrderId(1L);
        orderResponse.setEmail("test@gmail.com");

    }

    @Test
    public void testCheckout_Success() {
        when(userSessionService.getUserFromSession(session)).thenReturn(user);
        when(orderService.checkout(user)).thenReturn(orderResponse);
        ResponseEntity<OrderResponse> response = orderController.checkout(session);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testCheckout_UserNotLoggedIn() {
        when(userSessionService.getUserFromSession(session)).thenReturn(null);
        ResponseEntity<OrderResponse> response = orderController.checkout(session);
    }

}
