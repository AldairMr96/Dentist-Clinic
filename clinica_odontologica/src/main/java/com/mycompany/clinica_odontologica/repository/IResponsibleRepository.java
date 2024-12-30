package com.mycompany.clinica_odontologica.repository;

import com.mycompany.clinica_odontologica.model.Responsible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IResponsibleRepository extends JpaRepository< Responsible,Long> {
}
