package com.edtech.plugtify.repository;

import com.edtech.plugtify.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Token Repository
 */

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
}
