package com.mycompany.clinica_odontologica.repository;

import com.mycompany.clinica_odontologica.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IPatientRepository extends JpaRepository<Patient, Long > {
    List<Patient> findDistinctByTurnsPatient_DateTurn(LocalDate date);
}
