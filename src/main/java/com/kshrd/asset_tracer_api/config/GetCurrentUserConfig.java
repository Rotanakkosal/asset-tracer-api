package com.kshrd.asset_tracer_api.config;

import com.kshrd.asset_tracer_api.exception.UnauthorizedExceptionHandler;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class GetCurrentUserConfig {
    public UUID getCurrentUser() {

        Object getContext = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(getContext.equals("anonymousUser")) {
            throw new UnauthorizedExceptionHandler("Unauthorized User");
        }

        UserApp user = (UserApp) getContext;
        return user.getId();
    }
}
