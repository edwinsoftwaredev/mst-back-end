package com.edtech.plugtify.config;

import com.edtech.plugtify.security.AuthorityConstants;
import com.edtech.plugtify.security.DomainUserDetailsService;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.annotation.PostConstruct;

/**
 * Component to configure the security of the application.
 * -- Declare the type of authentication manager
 * -- Type of Authentication  -> Http Basic Authentication
 * -- filtering requests
 */

@Configuration
@EnableWebSecurity
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserDetailsService userDetailsService;
    private SecurityProblemSupport securityProblemSupport;

    public SecurityConfiguration(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            UserDetailsService userDetailsService,
            SecurityProblemSupport securityProblemSupport
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDetailsService = userDetailsService;
        this.securityProblemSupport = securityProblemSupport;
    }

    /**
     * the execution of this method is after depency injection is done and before any
     * other initialization
     */
    @PostConstruct
    public void init() {
        try {
            // type authentication definition
            this.authenticationManagerBuilder
                    .userDetailsService(this.userDetailsService)
                    .passwordEncoder(this.passwordEncoder());
        } catch(Exception e) {
            throw new BeanInitializationException("Security Configuration Fails: " + e);
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic()
            .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(this.securityProblemSupport)
                .accessDeniedHandler(this.securityProblemSupport)
            .and()
                .logout()
                .logoutUrl("/api/logout")
            .and()
                .authorizeRequests()
                    .antMatchers("/", "/login").permitAll()
                    .antMatchers("/api/register").permitAll()
                    .antMatchers("/api/csrf-token").permitAll()
                    .antMatchers("/api/**").authenticated()
                    .antMatchers("/api/managment/**").hasAnyAuthority(AuthorityConstants.ROLE_ADMIN)
                    .anyRequest().denyAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
