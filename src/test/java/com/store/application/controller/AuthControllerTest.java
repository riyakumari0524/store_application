package com.store.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.store.application.dto.UserRequest;
import com.store.application.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.store.application.service.UserService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthController authController;
    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
        userRequest.setEmail("testuser@example.com");
        userRequest.setPassword("password123");

        userResponse = new UserResponse();
        userResponse.setUserId(1L);
        userResponse.setEmail("test@gmail.com");

        session = mock(HttpSession.class);

    }

    @Test
    void testRegisterUser_Success() {
        when(userService.registerUser(userRequest)).thenReturn(userResponse);
        ResponseEntity<UserResponse> response = authController.registerUser(userRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
    }

    @Test
    void testLoginUser_Success() {
        String email = "testuser@example.com";
        String password = "password123";
        when(userService.loginUser(email, password, session)).thenReturn(userResponse);
        ResponseEntity<UserResponse> response = authController.loginUser(userRequest, session);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
    }

}
