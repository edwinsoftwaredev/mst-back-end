package com.edtech.plugtify.service.dto;

import javax.validation.constraints.Size;

/**
 * ManagedUserVM DTO -- class to define a user with password
 */

public class ManagedUserVM extends UserDTO {

    @Size(min = 6, max = 60)
    private String password;

    // Empty constructor needed for jackson
    public ManagedUserVM() { }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ManageUserVM{}" + super.toString();
    }
}
