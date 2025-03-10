package com.store.application.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.store.application.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.store.application.dto.UserRequest;
import com.store.application.dto.UserResponse;
import com.store.application.entity.User;
import com.store.application.exception.IncorrectPasswordException;
import com.store.application.exception.NotFoundException;
import com.store.application.exception.UserAlreadyExistsException;
import com.store.application.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpSession session;

    @InjectMocks
    private UserServiceImpl userService;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void testRegisterUser_Success() {
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        UserResponse response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals(request.getEmail(), response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        UserRequest request = new UserRequest();
        request.setEmail("existing@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginUser_Success() {
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(session.getId()).thenReturn("session123");

        UserResponse response = userService.loginUser(email, password, session);

        assertNotNull(response);
        assertEquals(user.getId(), response.getUserId());
        assertEquals(user.getEmail(), response.getEmail());
        verify(session, times(1)).setAttribute(anyString(), anyLong());
    }

    @Test
    void testLoginUser_UserNotFound() {
        String email = "nonexistent@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.loginUser(email, password, session));
        verify(session, never()).setAttribute(anyString(), anyLong());
    }

    @Test
    void testLoginUser_IncorrectPassword() {
        String email = "test@example.com";
        String correctPassword = "password123";
        String wrongPassword = "wrongPassword";
        String encodedPassword = passwordEncoder.encode(correctPassword);

        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(IncorrectPasswordException.class, () -> userService.loginUser(email, wrongPassword, session));
        verify(session, never()).setAttribute(anyString(), anyLong());
    }
}
