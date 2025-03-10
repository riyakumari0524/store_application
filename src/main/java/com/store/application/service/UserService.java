package com.store.application.service;

import com.store.application.dto.*;
import jakarta.servlet.http.HttpSession;

public interface UserService {
    public UserResponse registerUser(UserRequest user);

    public UserResponse loginUser(String email, String password, HttpSession session);

}
