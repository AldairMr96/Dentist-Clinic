package com.mycompany.springsecurity.authenticationSystem.repository;

import com.mycompany.springsecurity.authenticationSystem.model.Role;
import com.mycompany.springsecurity.authenticationSystem.model.RolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleTypeEnum(RolesEnum roleTypeEnum);


}
