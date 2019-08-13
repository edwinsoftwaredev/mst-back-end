package com.edtech.plugtify.repository;

import com.edtech.plugtify.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    String USER_BY_LOGIN_CACHE = "userByLogin";
    String USER_BY_EMAIL_CACHE = "userByEmail";

    Optional<User> findOneByLogin(String login);

    Optional<User> findOneByEmail(String email);

    // remember get authorities

    @Cacheable(cacheNames = USER_BY_LOGIN_CACHE)
    Optional<User> findOneByLoginIgnoreCase(String login);

    @Cacheable(cacheNames = USER_BY_EMAIL_CACHE)
    Optional<User> findOneByEmailIgnoreCase(String email);
}
