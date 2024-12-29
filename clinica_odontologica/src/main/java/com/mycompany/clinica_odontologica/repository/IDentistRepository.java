package com.mycompany.clinica_odontologica.repository;

import com.mycompany.clinica_odontologica.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDentistRepository extends JpaRepository<Long, Dentist> {

}
