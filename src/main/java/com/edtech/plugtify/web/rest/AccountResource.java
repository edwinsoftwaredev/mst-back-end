package com.edtech.plugtify.web.rest;

import com.edtech.plugtify.service.UserService;
import com.edtech.plugtify.service.dto.ManagedUserVM;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private UserService userService;

    public AccountResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * Resource to register a new User
     * @param managedUserVM userDTO with password --> Describe an User Entity
     * @return HttpResponse with status code OK
     */
    @PostMapping("/account")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody ManagedUserVM managedUserVM) {
        this.userService.registerUser(managedUserVM, managedUserVM.getPassword());

        // Return a HttpResponse with status OK if there is no Exception before
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
