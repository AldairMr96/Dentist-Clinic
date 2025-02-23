package com.mycompany.clinica_odontologica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Patient extends Person{

    @Enumerated(EnumType.STRING)
    private MedicalInsuranceTypeEnum medicalInsuranceType;
    private String bloodType;
    @OneToMany (mappedBy = "patient")
    private List<Turn> turnsPatient;
    @OneToMany (mappedBy = "relationshipPatient")
    private List<Responsible> resonsibles;

}
