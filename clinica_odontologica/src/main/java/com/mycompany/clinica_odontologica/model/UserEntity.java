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
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUser;
    private  String username;
    private String password;
    private Boolean isEnable;
    private Boolean accountNoExpired;
    private Boolean accountNoLocked;
    private Boolean creadentialNoExpired;

    @ManyToOne
    @JoinColumn(name = "role_id_user", nullable = false)
    private Role roleType;


}
