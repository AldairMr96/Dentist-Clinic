package com.mycompany.springsecurity.authenticationSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role_permission")
public class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRolePermission;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRole", nullable = false)
    @JsonBackReference
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idPermission", nullable = false)
    private Permission permission;
}
