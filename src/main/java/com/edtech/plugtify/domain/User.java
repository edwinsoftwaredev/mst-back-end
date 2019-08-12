package com.edtech.plugtify.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="tbl_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "login", length = 50, nullable = false)
    @Size(min = 4, max = 50)
    @Pattern(regexp = "^[_.@A-Za-z0-9-]+$", message = "login pattern dont match")
    private String login;

    
}
