package com.edtech.plugtify.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class AuthorizationCodeDTO {

    @NotBlank
    private String grant_type;

    @NotBlank
    private String code;

    @NotBlank
    private String redirect_uri;

    public AuthorizationCodeDTO(String grant_type, String code, String redirect_uri) {
        this.grant_type = grant_type;
        this.code = code;
        this.redirect_uri = redirect_uri;
    }
}
