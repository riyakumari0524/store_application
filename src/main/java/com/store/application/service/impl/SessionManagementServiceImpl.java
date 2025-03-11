package com.store.application.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.application.exception.NotFoundException;
import com.store.application.entity.User;
import com.store.application.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import com.store.application.dto.Constants;
import com.store.application.service.SessionManagementService;

@Service
public class SessionManagementServiceImpl implements SessionManagementService {


    private UserRepository userRepository;

    @Autowired
    private SessionManagementServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public User getUserFromSession(HttpSession session) {
        Long userId = getUserIdFromSession(session);
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));
    }

    private Long getUserIdFromSession(HttpSession session) {
        Object userId = session.getAttribute(Constants.USER_SESSION_ID);
        if (userId instanceof Long) {
            return (Long) userId;
        }
        throw new NotFoundException(Constants.USER_NOT_FOUND);
    }

}