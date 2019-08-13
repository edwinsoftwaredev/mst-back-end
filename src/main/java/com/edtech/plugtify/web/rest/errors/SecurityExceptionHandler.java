package com.edtech.plugtify.web.rest.errors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

@ControllerAdvice
public class SecurityExceptionHandler implements SecurityAdviceTrait {
}
