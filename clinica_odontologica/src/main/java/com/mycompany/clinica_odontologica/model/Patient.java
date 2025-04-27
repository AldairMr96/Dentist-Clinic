package com.mycompany.clinica_odontologica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patients")
public class Patient extends Person{

    @Enumerated(EnumType.STRING)
    private MedicalInsuranceTypeEnum medicalInsuranceType;
    private String bloodType;
    @OneToMany (mappedBy = "patient", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Turn> turnsPatient;
    @OneToMany (mappedBy = "relationshipPatient", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Responsible> resonsibles;

}
