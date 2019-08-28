package com.edtech.plugtify.web.rest;

import com.edtech.plugtify.repository.UserRepository;
import com.edtech.plugtify.service.SpotifyService;
import com.edtech.plugtify.service.UserService;
import com.edtech.plugtify.service.dto.ManagedUserVM;
import com.edtech.plugtify.service.dto.UserDTO;
import com.edtech.plugtify.web.rest.errors.InternalServerErrorException;
import com.edtech.plugtify.web.rest.errors.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private UserService userService;
    private SpotifyService spotifyService;
    private UserRepository userRepository;

    public AccountResource(
            UserService userService,
            SpotifyService spotifyService,
            UserRepository userRepository
    ) {
        this.userService = userService;
        this.spotifyService = spotifyService;
        this.userRepository = userRepository;
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

    /**
     * Method to delete the current user account
     * @return Http Response
     */
    @DeleteMapping("/delete-account/{login}")
    public ResponseEntity<Void> deleteCurrentUserAccount(@PathVariable("login") String login) {

        this.spotifyService.unfollowPlaylist(login);

        this.userService.deleteUser(login);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
