package com.mycompany.springsecurity.authenticationSystem.repository;

import com.mycompany.springsecurity.authenticationSystem.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Long> {
}
