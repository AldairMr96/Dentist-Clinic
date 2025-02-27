package com.mycompany.springsecurity.authenticationSystem.repository;

import com.mycompany.springsecurity.authenticationSystem.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRolePermissionRepository extends JpaRepository<RolePermission, Long> {
}
