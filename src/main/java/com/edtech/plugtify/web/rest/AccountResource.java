package com.edtech.plugtify.web.rest;

import com.edtech.plugtify.service.UserService;
import com.edtech.plugtify.service.dto.ManagedUserVM;
import com.edtech.plugtify.service.dto.UserDTO;
import com.edtech.plugtify.web.rest.errors.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody ManagedUserVM managedUserVM) {
        this.userService.registerUser(managedUserVM, managedUserVM.getPassword());

        // Return a HttpResponse with status OK if there is no Exception before
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @return a UserDTO from a User that is in securityContext
     */
    @GetMapping("/account")
    public UserDTO getCurrentUser() {
        return this.userService.getCurrentUser()
                .map(UserDTO::new)
                .orElseThrow(() -> new InternalServerErrorException("User can not be found!"));
    }
}
