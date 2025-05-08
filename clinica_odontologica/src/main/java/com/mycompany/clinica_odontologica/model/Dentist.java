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

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dentists")
public class Dentist extends Person{
    private String speciality;
    @OneToOne
    @JoinColumn(name = "id_user_dentist")
    @JsonIgnore
    private UserEntity dentistUserEntity;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="id_scheduel_dentist",referencedColumnName = "idSchedule")
    private Schedule scheduleDentist;
    @OneToMany (mappedBy = "dentist", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("turns_dentist")
    private List<Turn> turnsDentist;
 }
