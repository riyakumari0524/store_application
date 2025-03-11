package com.store.application.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import com.store.application.exception.UnauthorizedAccessException;
import com.store.application.annotation.RoleRequired;
import com.store.application.dto.Constants;
import com.store.application.service.impl.AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RoleCheck {

    private final AuthorizationService authorizationService;

    @Before("@annotation(roleRequired)")
    public void checkRole(RoleRequired roleRequired) {
        if (!authorizationService.hasRequiredRole(roleRequired.value())) {
            log.warn("Unauthorized access attempt detected for roles: {}", (Object) roleRequired.value());
            throw new UnauthorizedAccessException(Constants.UNAUTHORIZED_ACCESS);
        }
    }
}
