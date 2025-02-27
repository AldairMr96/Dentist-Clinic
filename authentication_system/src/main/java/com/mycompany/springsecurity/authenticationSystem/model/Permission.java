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
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPermission;

    @Column(unique = true, nullable = false, updatable = false)
    private String namePermission;



    @OneToMany(mappedBy = "permission", fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonBackReference
    private Set<RolePermission> rolePermissions = new HashSet<>();
}
