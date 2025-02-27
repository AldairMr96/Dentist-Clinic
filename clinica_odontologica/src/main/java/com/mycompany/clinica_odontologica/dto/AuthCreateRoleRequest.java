package com.mycompany.clinica_odontologica.dto;

import org.springframework.validation.annotation.Validated;

@Validated
public record AuthCreateRoleRequest(String  roleName) {
}
