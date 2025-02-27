package com.mycompany.springsecurity.authenticationSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private RolesEnum roleTypeEnum;

    @OneToMany(mappedBy = "roleType", fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonBackReference
    private Set<UserEntity> userEntities = new HashSet<>();


    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermission> rolePermissions = new HashSet<>();


}
