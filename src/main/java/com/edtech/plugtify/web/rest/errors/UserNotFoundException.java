package com.edtech.plugtify.web.rest.errors;

public class UserNotFoundException extends BadRequestAlertException {

    public UserNotFoundException() {
        super(ErrorConstants.USER_NOT_FOUND, "User not found!", "user", "userNotFound");
    }
}
