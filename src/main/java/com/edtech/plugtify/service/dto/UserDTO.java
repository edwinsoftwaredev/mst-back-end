package com.edtech.plugtify.service.dto;

import com.edtech.plugtify.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

/**
 * User DTO -- class to define a user without a password
 */

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @NotNull
    @Pattern(regexp = "^[_.@A-Za-z0-9-]+$")
    @Size(min = 4, max = 50)
    private String login;

    @Email(message = "Email is invalid")
    @NotBlank
    @Size(max = 254)
    private String email;

    private Boolean hasToken;

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.hasToken = user.getHasToken();
    }
}
