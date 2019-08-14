package com.edtech.plugtify.security;


import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() { } // private constructor to not let create instances of this class

    // method to obtain the current User from the current spring security context
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext(); // getting current security context

        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof UserDetails) {
                        UserDetails securityUserDetails = (UserDetails) authentication.getPrincipal(); // cast because .getPrincipal() return Object
                        return securityUserDetails.getUsername();
                    } else if(authentication.getPrincipal() instanceof  String) {
                        return (String) authentication.getPrincipal(); // cast because .getPrincipal() return Object
                    }

                    return null;
                });
    }

}
