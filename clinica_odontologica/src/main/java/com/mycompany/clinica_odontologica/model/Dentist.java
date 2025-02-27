package com.mycompany.clinica_odontologica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Dentist extends Person{
    private String speciality;
    @OneToOne
    private UserEntity dentistUserEntity;
    @OneToOne
    private Schedule scheduleDentist;
    @OneToMany (mappedBy = "dentist")
    private List<Turn> turnsDentist;
 }
