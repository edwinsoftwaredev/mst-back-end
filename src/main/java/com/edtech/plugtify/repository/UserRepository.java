package com.edtech.plugtify.repository;

import com.edtech.plugtify.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByLoginIgnoreCase(String login);

    Optional<User> findOneByEmailIgnoreCase(String email);
}
