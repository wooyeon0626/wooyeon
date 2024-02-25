package com.wooyeon.yeon.common.security;

import com.wooyeon.yeon.exception.ExceptionCode;
import com.wooyeon.yeon.exception.WooyeonException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();

        if (authentication == null || authentication.getName() == null) {
            throw new WooyeonException(ExceptionCode.SECURITY_CONTEXT_IS_EMPTY);
        }

        return userId;
    }
}
