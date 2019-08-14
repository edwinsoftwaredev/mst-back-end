package com.edtech.plugtify.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    // this method set the createdBy and lastUpdatedBy fields of AbstractAuditingEntity.
    // For example, if a user is being register then the createdBy field will have the login value of the current User
    // that is in the current security context.
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM_ACCOUNT));
    }
}
