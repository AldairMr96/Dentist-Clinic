package com.mycompany.clinica_odontologica.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    private List<Turn> turnsPatient;
    @OneToMany (mappedBy = "relationshipPatient", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Responsible> resonsibles;
    @OneToOne
    @JoinColumn(name = "id_user_patient")
    private UserEntity userEntityPatient;

}
