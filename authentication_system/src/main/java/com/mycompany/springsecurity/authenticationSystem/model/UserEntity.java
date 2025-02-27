package com.mycompany.springsecurity.authenticationSystem.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUser;

    @Column(unique = true)
    private String username;

    private String password;

    private Boolean isEnable;

    private Boolean accountNoExpired;

    private Boolean accountNoLocked;

    private Boolean creadentialNoExpired;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id_user", nullable = false)
    private Role roleType;
}
