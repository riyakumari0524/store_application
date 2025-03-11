package com.store.application.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.store.application.exception.*;
import com.store.application.entity.*;
import com.store.application.repository.*;
import com.store.application.dto.*;
import com.store.application.service.*;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        logger.info("Attempting to register user with email: {}", userRequest.getEmail());

        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            logger.error("User already exists with email: {}", userRequest.getEmail());
            throw new DuplicateUserException(Constants.USER_ALREADY_EXISTS);
        }

        User newUser = new User();
        newUser.setEmail(userRequest.getEmail());
        String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        userRepository.save(newUser);
        UserResponse response = new UserResponse();
        response.setUserId(newUser.getId());
        response.setEmail(newUser.getEmail());
        logger.info("User registered successfully with email: {}", userRequest.getEmail());

        return response;
    }

    @Override
    public UserResponse loginUser(String email, String password, HttpSession session) {
        logger.info("Attempting to login user with email: {}", email);
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isPresent()) {
            User user = optional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                session.setAttribute(Constants.USER_SESSION_ID, user.getId());
                UserResponse response = new UserResponse();
                response.setUserId(user.getId());
                response.setEmail(user.getEmail());
                response.setSessionId(session.getId());


                logger.info("User logged in successfully with email: {}", email);
                return response;
            } else {
                logger.error("Incorrect password for email: {}", email);
                throw new InvalidPasswordException(Constants.INVALID_PASSWORD);
            }
        } else {
            logger.error("User not found with email: {}", email);
            throw new NotFoundException(Constants.USER_NOT_FOUND);
        }
    }

}
