package com.store.application.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.store.application.dto.*;
import com.store.application.service.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user", description = "This endpoint registers a new user with email and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration Successful"),
            @ApiResponse(responseCode = "409", description = "User already exists with the given email address")
    })
    @PostMapping(value = "/auth/user/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest user) {
        UserResponse result = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Login with register user", description = "This endpoint logs in a user using their email and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Successful"),
            @ApiResponse(responseCode = "401", description = "Incorrect password"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/auth/user/loginUser")
    public ResponseEntity<UserResponse> loginUser(@RequestBody UserRequest user, HttpSession session) {
        UserResponse sessionId = userService.loginUser(user.getEmail(), user.getPassword(), session);
        return ResponseEntity.status(HttpStatus.OK).body(sessionId);
    }
}
