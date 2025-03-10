package com.store.application.service;

import com.store.application.entity.User;
import jakarta.servlet.http.HttpSession;

public interface UserSessionService {
    public User getUserFromSession(HttpSession session);
}