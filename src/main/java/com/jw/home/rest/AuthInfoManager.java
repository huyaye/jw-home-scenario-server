package com.jw.home.rest;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthInfoManager {
    public static String getRequestMemId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
