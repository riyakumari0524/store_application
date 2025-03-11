package com.store.application.service.impl;

import com.store.application.entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    public Role getCurrentUserRole() {
        return Role.ADMIN;
    }

    public boolean hasRequiredRole(Role[] requiredRoles) {
        Role currentRole = getCurrentUserRole();
        for (Role role : requiredRoles) {
            if (role == currentRole) {
                return true;
            }
        }
        return false;
    }
}
