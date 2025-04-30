package com.mycompany.clinica_odontologica.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract  class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long idPerson;
    private String dni;
    private String name ;
    private String lastname;
    private  String numberPhone ;
    private  String address;
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;
}
