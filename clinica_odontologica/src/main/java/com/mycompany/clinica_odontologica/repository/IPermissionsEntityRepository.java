package com.mycompany.clinica_odontologica.repository;

import com.mycompany.clinica_odontologica.model.PermissionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPermissionsEntityRepository extends JpaRepository<PermissionsEntity, Long > {
}
