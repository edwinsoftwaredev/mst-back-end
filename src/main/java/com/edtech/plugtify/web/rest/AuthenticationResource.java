package com.edtech.plugtify.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class AuthenticationResource {

    /**
     * Method to get principal when user send it's credentials in login form
     * @param principal user logged
     * @return principal
     */
    @GetMapping("/user")
    public ResponseEntity<Principal> getAuthenticatedUsed(Principal principal) {
        return ResponseEntity.ok().body(principal);
    }
}
