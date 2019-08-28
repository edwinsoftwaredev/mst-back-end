package com.edtech.plugtify.service;

import com.edtech.plugtify.domain.User;
import com.edtech.plugtify.repository.UserRepository;
import com.edtech.plugtify.security.SecurityUtils;
import com.edtech.plugtify.service.dto.UserDTO;
import com.edtech.plugtify.web.rest.errors.EmailAlreadyUsedException;
import com.edtech.plugtify.web.rest.errors.LoginAlreadyUsedException;
import com.edtech.plugtify.web.rest.errors.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * component to manage users
 */

@Service
@Transactional
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private CacheManager cacheManager;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cacheManager = cacheManager;
    }

    /**
     *
     * @param userDTO user from front-end
     * @param password password from front-end
     * @return User
     *
     * Method to register a user
     */
    public User registerUser(UserDTO userDTO, String password) {

        this.userRepository.findOneByLogin(userDTO.getLogin().toLowerCase().trim()).ifPresent(user -> {
            // If login is found throw custom exception
            // throw username or login already used exception
            throw new LoginAlreadyUsedException();
        });

        this.userRepository.findOneByEmail(userDTO.getEmail().toLowerCase().trim()).ifPresent(user -> {
            // If email is found throw custom exception
            // throw user email already used excepcion
            throw new EmailAlreadyUsedException();
        });

        // create new User
        User newUser = new User();
        newUser.setLogin(userDTO.getLogin().toLowerCase().trim());
        newUser.setEmail(userDTO.getEmail().toLowerCase().trim());
        newUser.setPassword(this.passwordEncoder.encode(password));

        // set default false to hasToken
        newUser.setHasToken(false);

        this.userRepository.save(newUser);
        this.clearUserCaches(newUser);

        this.logger.debug("New User created: {}", newUser);

        return newUser;

    }

    /**
     * Method to delete a user
     * @param principalName login user
     * @return Response Http
     */
    public ResponseEntity<Void> deleteUser(String principalName) {

        Optional<User> user = this.userRepository.findOneByLogin(principalName);

        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        this.userRepository.delete(user.get());

        this.clearUserCaches(user.get());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional(readOnly = true) // this method is readOnly = true the transaction to delete a user that comes from this method will not have effect
    public Optional<User> getCurrentUser() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
    }

    // Clear cache by the given cache names
    private void clearUserCaches(User user) {
        Objects.requireNonNull(this.cacheManager.getCache(this.userRepository.USER_BY_LOGIN_CACHE)).evict(user.getLogin());
        Objects.requireNonNull(this.cacheManager.getCache(this.userRepository.USER_BY_EMAIL_CACHE)).evict(user.getEmail());
    }

}
