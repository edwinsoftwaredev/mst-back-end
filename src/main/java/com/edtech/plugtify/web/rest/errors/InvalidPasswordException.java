package com.edtech.plugtify.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Class to generate incorrect password exception
 */
public class InvalidPasswordException extends AbstractThrowableProblem {

    public InvalidPasswordException(String message) {
        super(ErrorConstants.INVALID_PASSWORD_TYPE, "Incorrect password", Status.BAD_REQUEST);
    }
}
