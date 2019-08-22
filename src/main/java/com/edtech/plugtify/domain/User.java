package com.edtech.plugtify.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Entity: User
 */

@Entity
@Table(name="tbl_user")
public class User extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "login", length = 50, nullable = false)
    @Size(min = 4, max = 50)
    @Pattern(regexp = "^[_.@A-Za-z0-9-]+$", message = "login pattern dont match")
    private String login;

    @NotBlank
    @Column(name = "email", length = 254, nullable = false)
    @Email(message = "Email not valid")
    private String email;

    @NotBlank
    @JsonIgnore
    @Size(min = 6, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "token_id", referencedColumnName = "id")
    @JsonIgnore
    private Token token;

    @Column(name = "has_token")
    private Boolean hasToken;

    @Override
    public String toString() {
        return "User{}";
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Boolean getHasToken() {
        return hasToken;
    }

    public void setHasToken(Boolean hasToken) {
        this.hasToken = hasToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
