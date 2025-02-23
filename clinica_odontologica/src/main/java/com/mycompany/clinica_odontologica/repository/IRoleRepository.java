package com.mycompany.clinica_odontologica.repository;

import com.mycompany.clinica_odontologica.model.Role;
import com.mycompany.clinica_odontologica.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleTypeEnum(RoleEnum roleTypeEnum);
}
