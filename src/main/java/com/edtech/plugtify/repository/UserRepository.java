package com.edtech.plugtify.repository;

import com.edtech.plugtify.domain.User;
import org.hibernate.annotations.Parameter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Query("select a from User a join fetch a.token b where b.lastUpdateTime < :now")
    Set<User> findAllTokensByDay(@Param("now") Timestamp checkTime);
}
