package com.store.application.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.store.application.dto.OrderResponse;
import com.store.application.entity.User;
import com.store.application.service.OrderService;
import com.store.application.service.SessionManagementService;
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
    private SessionManagementService userSessionService;

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
        // Arrange
        when(userSessionService.getUserFromSession(session)).thenReturn(user);
        when(orderService.checkout(user)).thenReturn(orderResponse);

        // Act
        ResponseEntity<?> response = orderController.checkout(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(OrderResponse.class, response.getBody());
        assertEquals(orderResponse, response.getBody());

        // Verify method calls
        verify(userSessionService, times(1)).getUserFromSession(session);
        verify(orderService, times(1)).checkout(user);
    }

    @Test
    public void testCheckout_UserNotLoggedIn() {
        // Arrange
        when(userSessionService.getUserFromSession(session)).thenReturn(null);

        // Act
        ResponseEntity<?> response = orderController.checkout(session);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User not logged in. Please log in first.", response.getBody());

        // Verify that orderService is NOT called
        verify(userSessionService, times(1)).getUserFromSession(session);
        verifyNoInteractions(orderService);
    }

    @Test
    public void testCheckout_ExceptionThrown() {
        // Arrange
        when(userSessionService.getUserFromSession(session)).thenReturn(user);
        when(orderService.checkout(user)).thenThrow(new RuntimeException("Checkout failed due to insufficient funds"));

        // Act
        ResponseEntity<?> response = orderController.checkout(session);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Checkout failed due to insufficient funds", response.getBody());

        // Verify method calls
        verify(userSessionService, times(1)).getUserFromSession(session);
        verify(orderService, times(1)).checkout(user);
    }
}
