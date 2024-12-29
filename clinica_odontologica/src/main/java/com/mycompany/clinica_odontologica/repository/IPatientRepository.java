package com.mycompany.clinica_odontologica.repository;

import com.mycompany.clinica_odontologica.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPatientRepository extends JpaRepository<Long, Patient> {

}
