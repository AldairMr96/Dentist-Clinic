package com.mycompany.clinica_odontologica.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthLogin(@NotBlank String username, @NotBlank String password ) {

}
