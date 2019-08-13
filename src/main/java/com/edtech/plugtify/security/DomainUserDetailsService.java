package com.edtech.plugtify.security;

import com.edtech.plugtify.repository.UserRepository;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * component to check and create in spring security context the user(UserDetails),
 * validating it´s username and password
 */

@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        String login = username.toLowerCase();

        if(new EmailValidator().isValid(login, null)) {
            return this.userRepository.findOneByEmailIgnoreCase(login)
                    .map(user -> this.createSpringSecurityUser(user))
                    .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " was not found!"));
        }

        return this.userRepository.findOneByLoginIgnoreCase(login)
                .map(user -> this.createSpringSecurityUser(user))
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " was not found!"));

    }

    public User createSpringSecurityUser(com.edtech.plugtify.domain.User user) {

        // remember set authorities

        return new User(
                user.getLogin(),
                user.getPassword(),
                new ArrayList<GrantedAuthority>());
    }
}
