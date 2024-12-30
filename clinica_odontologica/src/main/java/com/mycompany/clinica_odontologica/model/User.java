package com.mycompany.clinica_odontologica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUser;
    private  String username;
    private String password;
    @ManyToOne
    @JoinColumn(name = "", nullable = false)
    private Role roleType;

}
