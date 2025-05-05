package com.mycompany.clinica_odontologica.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @Column(nullable = false, unique = true)
    private  String username;
    @Column(nullable = false)
    private String password;
    private Boolean isEnable;
    private Boolean accountNoExpired;
    private Boolean accountNoLocked;
    private Boolean creadentialNoExpired;

    @ManyToOne
    @JoinColumn(name = "role_id_user", nullable = false)
    @JsonBackReference
    private Role roleType;


}
