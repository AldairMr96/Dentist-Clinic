package com.mycompany.clinica_odontologica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "responsible_per_patient")
public class Responsible extends  Person{

    private String relationshipType;
    @ManyToOne
    @JoinColumn(name = "idPatient")
    private Patient relationshipPatient;

}
