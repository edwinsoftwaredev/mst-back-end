package com.edtech.plugtify.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "tbl_token")
public class Token implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token")
    @JsonIgnore
    private String access_token;

    @Column(name = "token_type")
    @JsonIgnore
    private String token_type;

    @Column(name = "scope")
    @JsonIgnore
    private String scope;

    @Column(name = "expires_in")
    @JsonIgnore
    private Integer expires_in;

    @Column(name = "refresh_token")
    @JsonIgnore
    private String refresh_token;

    @Column(name = "last_update_time")
    @JsonIgnore
    private Timestamp lastUpdateTime = Timestamp.from(Instant.now());

    // commented, as it is a unidirectional relationship
    /* @OneToOne(mappedBy = "token") // mappedBy signal hibernate that the User entity has the foreign key
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "Token:{}";
    }
}
