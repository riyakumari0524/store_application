package com.store.application.service.impl;

import org.springframework.stereotype.Service;

import com.store.application.exception.NotFoundException;
import com.store.application.entity.User;
import com.store.application.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import com.store.application.dto.Constants;
import com.store.application.service.UserSessionService;

@Service
public class UserSessionServiceImpl implements UserSessionService {


    private UserRepository userRepository;

    private UserSessionServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public User getUserFromSession(HttpSession session) {
        Long userId = (Long) session.getAttribute(Constants.USER_ID_SESSION_ATTRIBUTE);
        if (userId == null) {
            throw new NotFoundException(Constants.USER_NOT_FOUND);
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));
    }

}