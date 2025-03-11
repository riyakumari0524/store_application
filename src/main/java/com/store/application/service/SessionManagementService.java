package com.store.application.service;

import com.store.application.entity.User;
import jakarta.servlet.http.HttpSession;

public interface SessionManagementService {
    public User getUserFromSession(HttpSession session);
}