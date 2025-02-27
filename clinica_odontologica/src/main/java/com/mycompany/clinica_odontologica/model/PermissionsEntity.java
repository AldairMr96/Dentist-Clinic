package com.mycompany.clinica_odontologica.model;

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
@Table(name = "permissions")
public class PermissionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPermissionEntity;
    @Column(unique = true, nullable = false, updatable = false)
    private String namePermission;

    @OneToMany(mappedBy = "permissionEntity", fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonBackReference
    private Set<RolePermissions> rolePermissions = new HashSet<>();

}
