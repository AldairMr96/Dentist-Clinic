package com.mycompany.springsecurity.authenticationSystem.dto;

import org.springframework.validation.annotation.Validated;

@Validated
public record AuthCreateRoleRequest  (
         String  roleName
        ) {
}
