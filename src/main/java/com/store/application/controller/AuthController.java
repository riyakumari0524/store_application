package com.store.application.controller;

import com.store.application.dto.UserRequest;
import com.store.application.dto.UserResponse;
import com.store.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/profile")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Registers a new user with email and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registration Successful"),
            @ApiResponse(responseCode = "409", description = "User already exists with the given email address")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest user) {
        UserResponse result = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Login a registered user", description = "Logs in a user using their email and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Successful"),
            @ApiResponse(responseCode = "401", description = "Incorrect password"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody UserRequest user, HttpSession session) {
        UserResponse sessionId = userService.loginUser(user.getEmail(), user.getPassword(), session);
        return ResponseEntity.status(HttpStatus.OK).body(sessionId);
    }
}
