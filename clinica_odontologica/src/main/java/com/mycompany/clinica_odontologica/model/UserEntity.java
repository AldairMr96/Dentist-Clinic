package com.mycompany.clinica_odontologica.model;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
