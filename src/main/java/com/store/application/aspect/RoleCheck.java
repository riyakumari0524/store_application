package com.store.application.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.store.application.exception.UnauthorizedAccessException;
import com.store.application.annotation.RoleRequired;
import com.store.application.dto.Constants;
import com.store.application.service.impl.AuthorizationService;

@Aspect
@Component
public class RoleCheck {

    @Autowired
    private AuthorizationService authorizationService;

    @Before("@annotation(roleRequired)")
    public void checkRole(RoleRequired roleRequired) {

        if (!authorizationService.hasRequiredRole(roleRequired.value())) {
            throw new UnauthorizedAccessException(Constants.UNAUTHORIZED_ACCESS_MESSAGE);
        }
    }
}
