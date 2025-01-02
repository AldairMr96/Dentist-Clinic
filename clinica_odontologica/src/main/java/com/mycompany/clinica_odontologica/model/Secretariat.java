package com.mycompany.clinica_odontologica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Secretariat extends Person {

    private String sector;

    @OneToOne
    private User secretariatUser;

}
