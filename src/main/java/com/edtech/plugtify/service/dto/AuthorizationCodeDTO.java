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
    private String code;

    public AuthorizationCodeDTO(String code) {
        this.code = code;
    }
}
