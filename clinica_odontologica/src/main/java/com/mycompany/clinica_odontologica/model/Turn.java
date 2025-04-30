package com.mycompany.clinica_odontologica.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="turns")
public class Turn {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long idTurn;
    @Temporal(TemporalType.DATE)
    private LocalDate dateTurn;
    private String shiftTime;
    private String disease;
    @ManyToOne
    @JoinColumn(name="idTurnDentist")
    private Dentist dentist;
    @ManyToOne
    @JoinColumn(name = "idTurnPatient")
    @JsonBackReference("patient_reference")
    private Patient patient;

}
