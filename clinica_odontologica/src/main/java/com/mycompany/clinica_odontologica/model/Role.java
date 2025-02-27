package com.mycompany.clinica_odontologica.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idRole;
    @Enumerated(EnumType.STRING)
    private RoleEnum roleTypeEnum;

    @OneToMany (mappedBy = "roleType", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<UserEntity> userEntities = new HashSet<>();

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private  Set<RolePermissions> rolePermissions = new HashSet<>();


}
